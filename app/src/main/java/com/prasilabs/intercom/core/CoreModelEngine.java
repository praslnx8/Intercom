package com.prasilabs.intercom.core;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.enums.ErrorCode;
import com.prasilabs.intercom.services.network.NetworkManager;
import com.prasilabs.intercom.utils.ViewUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by prasi on 27/5/16.
 */
public abstract class CoreModelEngine
{
    private static final String TAG = CoreModelEngine.class.getSimpleName();

    protected <T> void callAsync(final AsyncCallBack asyncCallBack)
    {
        callAsync(asyncCallBack, false);
    }

    protected void makeVolleyCall(String url, JSONObject jsonObject, final ApiCallBack apiCallBack)
    {
        /*VolleySingleton.VolleyCallBack volleyCallBack =  new VolleySingleton.VolleyCallBack() {
            @Override
            public void result(String result)
            {
                if (apiCallBack != null)
                {
                    try
                    {
                        apiCallBack.result(result);
                    }
                    catch (Exception e)
                    {
                        ConsoleLog.e(e);
                        apiCallBack.error(ErrorCode.GENERAL);
                    }
                }
            }

            @Override
            public void error(ErrorCode errorCode) {
                if (apiCallBack != null) {
                    apiCallBack.error(errorCode);
                }
            }
        };

        VolleySingleton.getInstance().makeStringRequest(url, jsonObject, volleyCallBack);*/
    }

    protected void makeVolleyCall(int mode, String url, Map<String, String> mParams, final ApiCallBack apiCallBack)
    {
        /*VolleySingleton.VolleyCallBack volleyCallBack =  new VolleySingleton.VolleyCallBack() {
            @Override
            public void result(String result)
            {
                if (apiCallBack != null)
                {
                    try
                    {
                        apiCallBack.result(result);
                    }
                    catch (Exception e)
                    {
                        ConsoleLog.e(e);
                        apiCallBack.error(ErrorCode.GENERAL);
                    }
                }
            }

            @Override
            public void error(ErrorCode errorCode) {
                if (apiCallBack != null) {
                    apiCallBack.error(errorCode);
                }
            }
        };


        VolleySingleton.getInstance().makeStringRequest(mode, url, mParams,volleyCallBack);*/
    }

    protected <T> void callAsync(final AsyncCallBack asyncCallBack, final boolean isBackgroundCall)
    {
        boolean isOnline = new NetworkManager(CoreApp.getAppContext(), /*new NetworkManager.NetworkHandler() {
            @Override
            public void onNetworkUpdate(boolean isOnline)
            {
                if(isBackgroundCall)
                {
                    call(asyncCallBack);
                }
            }
        }*/ null).isOnline();

        if (isOnline) {
            call(asyncCallBack);
        } else if (!isBackgroundCall) {
            ViewUtil.t(CoreApp.getAppContext(), "Please check the network and try again");

            if (asyncCallBack != null) {
                asyncCallBack.error(ErrorCode.NOT_CONNECTED);
            }
        }
    }

    protected void sendBroadCast(Intent intent)
    {
        if(CoreApp.getAppContext() != null)
        {
            LocalBroadcastManager.getInstance(CoreApp.getAppContext()).sendBroadcast(intent);
        }
        else
        {
            ConsoleLog.w(TAG, "context is null");
        }
    }


    private <T> void call(final AsyncCallBack asyncCallBack)
    {
        new AsyncTask<Void, Void, T>()
        {
            private ErrorCode errorCode = ErrorCode.GENERAL;
            private boolean isError;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected T doInBackground(Void... params)
            {
                try
                {
                    if (asyncCallBack != null)
                    {
                        return asyncCallBack.async();
                    }
                } catch (Exception e)
                {
                    ConsoleLog.e(e);

                    isError = true;
                    if (e instanceof IOException)
                    {
                        errorCode = ErrorCode.TIME_OUT;
                    }
                    else
                    {
                        errorCode = ErrorCode.GENERAL;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(T t)
            {
                super.onPostExecute(t);

                if(asyncCallBack != null)
                {
                    if (isError)
                    {
                        asyncCallBack.error(errorCode);
                    } else {
                        asyncCallBack.result(t);
                    }
                }
            }
        }.execute();
    }

    public <T> void runAsync(final RunAsyncCallBack runAsyncCallBack)
    {
        new AsyncTask<Void, Void, T>()
        {

            @Override
            protected T doInBackground(Void... params)
            {
                if(runAsyncCallBack != null)
                {
                    try
                    {
                        return runAsyncCallBack.run();
                    }
                    catch (Exception e)
                    {
                        ConsoleLog.e(e);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(T t)
            {
                super.onPostExecute(t);

                if(runAsyncCallBack != null)
                {
                    runAsyncCallBack.result(t);
                }
            }
        }.execute();
    }

    public interface AsyncCallBack
    {
        <T> T async() throws Exception;

        <T> void result(T t);

        void error(ErrorCode errorCode);
    }

    public interface ApiCallBack
    {
        void result(String result) throws Exception;

        void error(ErrorCode errorCode);
    }

    public interface RunAsyncCallBack
    {
        <T> T run() throws Exception;

        <T> void result(T t);
    }
}
