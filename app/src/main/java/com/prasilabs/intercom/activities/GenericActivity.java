package com.prasilabs.intercom.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreActivity;
import com.prasilabs.intercom.core.CoreFragment;
import com.prasilabs.intercom.core.CorePresenter;
import com.prasilabs.intercom.customs.FragmentNavigator;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.modules.call.view.CallFragment;
import com.prasilabs.intercom.pojo.UserInfo;

import butterknife.BindView;

/**
 * Created by prasi on 10/6/16.
 */
public class GenericActivity extends CoreActivity
{
    private static final String REQUEST_FOR = "requestFor";
    private static final String ID_STR = "id";

    public static final int REQUEST_FOR_CALL = 1;


    private int requestFor = 0;
    private int id = 0;

    private CoreFragment coreFragment;

    private static final String TAG = GenericActivity.class.getSimpleName();
    @BindView(R.id.container)
    LinearLayout container;

    public static void openCall(Context context, UserInfo userInfo, boolean isIncoming)
    {
        Intent intent = new Intent(context, GenericActivity.class);
        intent.putExtra(REQUEST_FOR, REQUEST_FOR_CALL);
        intent.putExtra("IP", userInfo.getIp());
        intent.putExtra("NAME", userInfo.getName());
        intent.putExtra("IMAGE", userInfo.getPic());
        intent.putExtra("IS_INCOMING", isIncoming);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            requestFor = bundle.getInt(REQUEST_FOR, 0);
            id = bundle.getInt(ID_STR, 0);
        }


        if(requestFor == REQUEST_FOR_CALL)
        {
            if(getSupportActionBar() != null)
            {
                getSupportActionBar().hide();
            }


            String ip = bundle.getString("IP");
            String name = bundle.getString("NAME");
            String image = bundle.getString("IMAGE");
            boolean isIncoming = bundle.getBoolean("IS_INCOMING");

            UserInfo userInfo = new UserInfo();
            userInfo.setIp(ip);
            userInfo.setName(name);
            userInfo.setPic(image);

            coreFragment = CallFragment.newInstance(userInfo, isIncoming);
            FragmentNavigator.navigateToFragment(this, coreFragment, false, container.getId());
        }
        else
        {
            ConsoleLog.w(TAG, "inappropiate call");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(coreFragment != null)
        {
            coreFragment.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected CorePresenter setCorePresenter()
    {
        return null;
    }

    @Override
    public void onBackPressed()
    {
        if(coreFragment == null || coreFragment.onBackPressed())
        {
            super.onBackPressed();
        }
    }

    private void backPress()
    {
        super.onBackPressed();
    }

    public void showToolBar(String title)
    {
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(coreFragment != null)
        {
            coreFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public int getContainerId()
    {
        return container.getId();
    }
}
