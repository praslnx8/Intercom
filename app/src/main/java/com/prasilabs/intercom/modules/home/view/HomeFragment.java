package com.prasilabs.intercom.modules.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.audioserver.ScanIP;
import com.prasilabs.intercom.core.CoreFragment;
import com.prasilabs.intercom.customs.CircularProgressBar;
import com.prasilabs.intercom.modules.home.presenter.HomePresenter;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.services.network.IntercomWifiManager;

import java.util.List;

import butterknife.BindView;

/**
 * Created by prasi on 31/8/16.
 */
public class HomeFragment extends CoreFragment<HomePresenter> implements HomePresenter.HomePresenterCallBack
{
    private static HomeFragment homeFragment;

    @BindView(R.id.wifi_text)
    protected TextView wifiText;
    @BindView(R.id.ip_text)
    protected TextView ipText;
    @BindView(R.id.circularprogressbar)
    protected CircularProgressBar circularProgressBar;

    public static HomeFragment getInstance()
    {
        if(homeFragment == null)
        {
            homeFragment = new HomeFragment();
        }

        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_home, container, false));

            ScanIP scanIP = new ScanIP(getContext());
            //scanIP.start();

            getPresenter().init();
        }

        return getFragmentView();
    }

    @Override
    protected HomePresenter setCorePresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void setWifiName()
    {
        String wifiName = IntercomWifiManager.getWifiName(getContext());

        String ip = IntercomWifiManager.getWifiIp(getContext());

        if(wifiName != null)
        {
            wifiText.setText(wifiName);
        }
        if(ip != null)
        {
            ipText.setText(ip);
        }
    }

    @Override
    public void startScan() {

    }

    @Override
    public void showScanStats(int percetage, int users)
    {
        circularProgressBar.setProgress(percetage);
        circularProgressBar.setTitle(String.valueOf(users));
    }

    @Override
    public void showUsersList(List<UserInfo> userInfos)
    {

    }
}
