package com.prasilabs.intercom.modelEngines;

import android.text.TextUtils;

import com.prasilabs.intercom.constants.CommonConstant;
import com.prasilabs.intercom.core.CoreApp;
import com.prasilabs.intercom.core.CoreModelEngine;
import com.prasilabs.intercom.customs.LocalPreference;

/**
 * Created by prasi on 23/7/16.
 */
public class CrashReportModelEngine extends CoreModelEngine
{
    private static final String TAG = CrashReportModelEngine.class.getSimpleName();

    private static CrashReportModelEngine instance;

    public static CrashReportModelEngine getInstance()
    {
        if(instance == null)
        {
            instance = new CrashReportModelEngine();
        }

        return instance;
    }

    public void reportCrashIfExist()
    {
        if(CoreApp.getAppContext() != null)
        {
            String stackTrace = LocalPreference.getAppDataFromShared(CoreApp.getAppContext(), CommonConstant.CRASH_STACKTRACE_STR, null);

            if(!TextUtils.isEmpty(stackTrace))
            {
                /*CrashReportIO crashReportIO = new CrashReportIO();
                crashReportIO.setEmail("medaho@dropme.com");
                crashReportIO.setAndroidVersion(Build.VERSION.CODENAME);
                crashReportIO.setCrashMessage(stackTrace);
                crashReportIO.setDeviceName(Build.MANUFACTURER);
                crashReportIO.setModelName(Build.MODEL);
                crashReportIO.setDeviceId(CoreApp.getDeviceId());
                crashReportIO.setCrashType("EXCEPTION");

                String jsonObjectStr = new Gson().toJson(crashReportIO);
                JSONObject jsonObject = JsonUtil.createjsonobject(jsonObjectStr);

                makeVolleyCall(UrlConstants.CRASH_URL, jsonObject, new CoreModelEngine.ApiCallBack() {
                    @Override
                    public void result(String result)
                    {
                        if(CoreApp.getAppContext() != null)
                        {
                            LocalPreference.saveAppDataInShared(CoreApp.getAppContext(), CommonConstant.CRASH_STACKTRACE_STR, "");
                        }
                        else
                        {
                            ConsoleLog.w(TAG, "context is null");
                        }
                    }

                    @Override
                    public void error(ErrorCode errorCode)
                    {
                        ConsoleLog.w(TAG, "error : " + errorCode.name());
                    }
                });*/
            }
        }
    }
}
