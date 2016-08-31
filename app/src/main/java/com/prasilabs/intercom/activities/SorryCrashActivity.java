package com.prasilabs.intercom.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.core.CoreActivity;
import com.prasilabs.intercom.core.CorePresenter;
import com.prasilabs.intercom.modelEngines.CrashReportModelEngine;

import butterknife.OnClick;

/**
 * Created by prasi on 23/6/16.
 */
public class SorryCrashActivity extends CoreActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sorry_crash);

        CrashReportModelEngine.getInstance().reportCrashIfExist();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Error occured");
        }
    }

    @OnClick(R.id.try_again_btn)
    protected void tryAgain()
    {
        //TODO SplashActivity.openSplashActivity(this);
        finish();
    }

    @OnClick(R.id.close_btn)
    protected void closeClicked() {
        finish();
        System.exit(0);
    }

    @Override
    protected CorePresenter setCorePresenter() {
        return null;
    }
}
