package com.prasilabs.intercom.modules.call.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreFragment;
import com.prasilabs.intercom.modules.call.presenter.CallPresenter;
import com.prasilabs.intercom.pojo.UserInfo;

/**
 * Created by prasi on 1/9/16.
 */
public class CallFragment extends CoreFragment<CallPresenter>
{
    private static final String IP_STR = "ip";
    private static final String NAME_STR = "name";
    private static final String IMAGE_STR = "image";
    private static final String IS_INCOMING_STR = "is_incoming";

    private boolean isIncoming;
    private UserInfo userInfo;

    public static CallFragment newInstance(UserInfo userInfo, boolean isIncoming)
    {
        CallFragment callFragment = new CallFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IP_STR, userInfo.getIp());
        bundle.putString(NAME_STR, userInfo.getName());
        bundle.putString(IMAGE_STR, userInfo.getPic());
        bundle.putBoolean(IS_INCOMING_STR, isIncoming);

        callFragment.setArguments(bundle);

        return callFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
        {
            String ip = getArguments().getString(IP_STR, null);
            String name = getArguments().getString(NAME_STR, null);
            String image = getArguments().getString(IMAGE_STR, null);

            isIncoming = getArguments().getBoolean(IS_INCOMING_STR, false);

            userInfo = new UserInfo();
            userInfo.setIp(ip);
            userInfo.setName(name);
            userInfo.setPic(image);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(getFragmentView() == null)
        {
            setFragmentView(inflater.inflate(R.layout.fragment_call, container, false));


        }

        return getFragmentView();
    }

    @Override
    protected CallPresenter setCorePresenter()
    {
        return new CallPresenter();
    }
}
