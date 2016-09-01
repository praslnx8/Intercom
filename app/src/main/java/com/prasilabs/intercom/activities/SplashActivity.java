package com.prasilabs.intercom.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreActivity;
import com.prasilabs.intercom.core.CorePresenter;
import com.prasilabs.intercom.customs.FragmentNavigator;
import com.prasilabs.intercom.managers.UserManager;
import com.prasilabs.intercom.modules.login.view.LoginFragment;

public class SplashActivity extends CoreActivity
{
    private LoginFragment loginFragment;

    public static void openSplashActivity(Context context)
    {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        UserManager.saveUserData(this, "prasanna", "https://lh5.googleusercontent.com/proxy/_ARsI9ITQGmTBBzEbppiBc6JRVfkcZbnTbCgXjgT77Ck0fy9iHTTpPmey1hXO2Q75unF9BfF6gCKQ3dz3IocxDTbzHp5rrBpr7_dV_7-JEIq=w96-h96-nc");

        loginFragment = LoginFragment.newInstance();
        FragmentNavigator.navigateToFragment(this, loginFragment, false, R.id.container);
    }

    @Override
    protected CorePresenter setCorePresenter()
    {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(loginFragment != null)
        {
            loginFragment.onActivityResult(requestCode, resultCode, data);
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
