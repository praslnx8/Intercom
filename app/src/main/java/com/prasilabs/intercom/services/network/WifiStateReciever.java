package com.prasilabs.intercom.services.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.prasilabs.intercom.constants.BroadCastConstant;
import com.prasilabs.intercom.debug.ConsoleLog;

public class WifiStateReciever extends BroadcastReceiver
{
    private static final String TAG = WifiStateReciever.class.getSimpleName();
    private static boolean isWifiEnabled = false;
    private static boolean isFirstTime = true;

    public WifiStateReciever()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConsoleLog.i(TAG, "wifi state changed");
        if(isFirstTime || !isWifiEnabled && IntercomWifiManager.isWifiConnected(context) || (isWifiEnabled && !IntercomWifiManager.isWifiConnected(context)))
        {
            isFirstTime = false;
            isWifiEnabled = !isWifiEnabled;

            Intent localIntent = new Intent();
            localIntent.setAction(BroadCastConstant.WIFI_REFRESH_INTENT);
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
        }
    }
}
