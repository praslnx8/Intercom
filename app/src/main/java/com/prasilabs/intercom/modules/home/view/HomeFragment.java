package com.prasilabs.intercom.modules.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreFragment;
import com.prasilabs.intercom.customs.CircularProgressBar;
import com.prasilabs.intercom.modules.home.presenter.HomePresenter;
import com.prasilabs.intercom.pojo.UserInfo;

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

    @BindView(R.id.no_users_layout)
    protected LinearLayout noUsersLayout;
    @BindView(R.id.list_view_layout)
    protected LinearLayout listViewLayout;
    @BindView(R.id.no_wifi_layout)
    protected LinearLayout noWifiLayout;
    @BindView(R.id.progress_layout)
    protected LinearLayout progressLayout;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.ip_list_view)
    ListView ipListView;


    private IpListAdapter ipListAdapter;

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

            ipListAdapter = new IpListAdapter(getContext());
            ipListView.setAdapter(ipListAdapter);

            getPresenter().init();
        }

        return getFragmentView();
    }

    @Override
    protected HomePresenter setCorePresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void setWifiName(String wifiName, String ipAddress)
    {
        if(wifiName != null)
        {
            wifiText.setText(wifiName);
        }
        if(ipAddress != null)
        {
            ipText.setText(ipAddress);
        }
    }

    @Override
    public void showEnableWifiScreen()
    {
        contentLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.GONE);
        noWifiLayout.setVisibility(View.VISIBLE);
        listViewLayout.setVisibility(View.GONE);
        noUsersLayout.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyPageScreen()
    {
        contentLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        noWifiLayout.setVisibility(View.GONE);
        listViewLayout.setVisibility(View.GONE);
        noUsersLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showScanning()
    {
        contentLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
        noWifiLayout.setVisibility(View.GONE);
        listViewLayout.setVisibility(View.GONE);
        noUsersLayout.setVisibility(View.GONE);
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
        contentLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        noWifiLayout.setVisibility(View.GONE);
        listViewLayout.setVisibility(View.VISIBLE);
        noUsersLayout.setVisibility(View.GONE);

        ipListAdapter.cearAndAddUsers(userInfos);
    }
}
