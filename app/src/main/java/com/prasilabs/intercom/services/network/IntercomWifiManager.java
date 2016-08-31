package com.prasilabs.intercom.services.network;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.prasilabs.intercom.debug.ConsoleLog;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by prasi on 31/8/16.
 */
public class IntercomWifiManager
{
    public static String getWifiName(Context context)
    {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled())
        {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }

    public static String getWifiIp(Context context)
    {
        try
        {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifiinfo = manager.getConnectionInfo();
            byte[] myIPAddress = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();
            Collections.reverse(Arrays.asList(myIPAddress));
            InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
            String myIP = myInetIP.getHostAddress();

            return myIP;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }
}
