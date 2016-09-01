package com.prasilabs.intercom.managers;

import android.content.Context;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.prasilabs.intercom.customs.LocalPreference;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.services.network.IntercomWifiManager;

/**
 * Created by prasi on 31/8/16.
 */
public class UserManager
{
    private static final String NAME_STR = "name";
    private static final String PICTURE_STR = "picture";
    private static final String TAG = UserManager.class.getSimpleName();

    public static void saveUserData(Context context, String name, String pictureUrl)
    {
        LocalPreference.saveLoginDataInShared(context, NAME_STR, name);
        LocalPreference.saveLoginDataInShared(context, PICTURE_STR, pictureUrl);
    }

    public static String getName(Context context)
    {
        return LocalPreference.getLoginDataFromShared(context, NAME_STR, null);
    }

    public static String getPicture(Context context)
    {
        return LocalPreference.getLoginDataFromShared(context, PICTURE_STR, null);
    }

    public static UserInfo getMyInfo(Context context)
    {
        UserInfo userInfo = new UserInfo();

        userInfo.setName(getName(context));
        userInfo.setPic(getPicture(context));
        userInfo.setIp(IntercomWifiManager.getWifiIp(context));

        return userInfo;
    }

    public static void flush(GoogleApiClient googleApiClient, final Context context, final LogoutCallBack logoutCallBack)
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status)
                    {
                        if(status.isSuccess()) {
                            LocalPreference.clearLoginSharedPreferences(context);
                        }

                        if(logoutCallBack != null)
                        {
                            logoutCallBack.status(status.isSuccess());
                        }

                        ConsoleLog.i(TAG, "logout status : " + status.isSuccess());
                    }
                });
    }

    public interface LogoutCallBack
    {
        void status(boolean success);
    }
}
