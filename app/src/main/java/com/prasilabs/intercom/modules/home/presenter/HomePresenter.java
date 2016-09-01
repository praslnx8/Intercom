package com.prasilabs.intercom.modules.home.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prasilabs.intercom.constants.BroadCastConstant;
import com.prasilabs.intercom.core.CorePresenter;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.modelEngines.UsersModelEngine;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.services.network.IntercomWifiManager;

import java.util.List;

/**
 * Created by prasi on 31/8/16.
 */
public class HomePresenter extends CorePresenter
{
    private static final String TAG = HomePresenter.class.getSimpleName();
    private HomePresenterCallBack homePresenterCallBack;

    public HomePresenter(HomePresenterCallBack homePresenterCallBack)
    {
        this.homePresenterCallBack = homePresenterCallBack;
    }

    @Override
    protected void onCreateCalled()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastConstant.WIFI_REFRESH_INTENT);
        intentFilter.addAction(BroadCastConstant.USER_CHANGE_REFRESH);

        registerReciever(intentFilter);
    }

    public void init()
    {
        checkWifiAndSetName();
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {
        ConsoleLog.i(TAG, "intent recieved");
        if(intent.getAction().equalsIgnoreCase(BroadCastConstant.WIFI_REFRESH_INTENT))
        {
            checkWifiAndSetName();
        }
        else if(intent.getAction().equalsIgnoreCase(BroadCastConstant.USER_CHANGE_REFRESH))
        {
            if(homePresenterCallBack != null)
            {
                List<UserInfo> userInfoList = UsersModelEngine.getInstance().getUserInfoList();

                if(userInfoList != null && userInfoList.size() > 0)
                {
                    homePresenterCallBack.showUsersList(userInfoList);
                }
                else
                {
                    homePresenterCallBack.showEmptyPageScreen();
                }
            }
        }
    }

    private void checkWifiAndSetName()
    {
        if(!IntercomWifiManager.isWifiConnected(getContext()))
        {
            if(homePresenterCallBack != null)
            {
                homePresenterCallBack.showEnableWifiScreen();
            }
        }
        else
        {
            String wifiName = IntercomWifiManager.getWifiName(getContext());

            String ip = IntercomWifiManager.getWifiIp(getContext());

            if(homePresenterCallBack != null)
            {
                homePresenterCallBack.setWifiName(wifiName, ip);
                homePresenterCallBack.showScanning();

                UsersModelEngine.getInstance().scanUsers(new UsersModelEngine.ScanUserCallBack() {
                    @Override
                    public void scaned(int ipCount, int userCount)
                    {
                        int percentage = ipCount*100/254;

                        if(homePresenterCallBack != null)
                        {
                            homePresenterCallBack.showScanStats(percentage, userCount);
                        }
                    }

                    @Override
                    public void usersFound(List<UserInfo> userInfos)
                    {
                        if(homePresenterCallBack != null)
                        {
                            if(userInfos != null && userInfos.size() > 0)
                            {
                                homePresenterCallBack.showUsersList(userInfos);
                            }
                            else
                            {
                                homePresenterCallBack.showEmptyPageScreen();
                            }
                        }
                    }
                });
            }
        }
    }

    public interface HomePresenterCallBack
    {
        void setWifiName(String wifiName, String ipAddr);

        void showEnableWifiScreen();

        void showEmptyPageScreen();

        void showScanning();

        void showScanStats(int percetage, int users);

        void showUsersList(List<UserInfo> userInfos);
    }
}
