package com.prasilabs.intercom.modules.home.presenter;

import android.content.Context;
import android.content.Intent;

import com.prasilabs.intercom.core.CorePresenter;
import com.prasilabs.intercom.modelEngines.UsersModelEngine;
import com.prasilabs.intercom.pojo.UserInfo;

import java.util.List;

/**
 * Created by prasi on 31/8/16.
 */
public class HomePresenter extends CorePresenter
{
    private HomePresenterCallBack homePresenterCallBack;

    public HomePresenter(HomePresenterCallBack homePresenterCallBack)
    {
        this.homePresenterCallBack = homePresenterCallBack;
    }

    @Override
    protected void onCreateCalled()
    {

    }

    public void init()
    {
        if(homePresenterCallBack != null)
        {
            homePresenterCallBack.setWifiName();
        }

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
                    homePresenterCallBack.showUsersList(userInfos);
                }
            }
        });
    }

    @Override
    protected void broadCastRecieved(Context context, Intent intent)
    {

    }

    public interface HomePresenterCallBack
    {
        void setWifiName();

        void startScan();

        void showScanStats(int percetage, int users);

        void showUsersList(List<UserInfo> userInfos);
    }
}
