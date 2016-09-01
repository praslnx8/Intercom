package com.prasilabs.intercom.services.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.prasilabs.intercom.debug.ConsoleLog;

import java.math.BigInteger;
import java.net.InetAddress;

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
            byte[] reversedAddress = new byte[myIPAddress.length];
            for(int i=0; i<myIPAddress.length; i++)
            {
                reversedAddress[i] = myIPAddress[(myIPAddress.length-1) - i];
            }

            InetAddress myInetIP = InetAddress.getByAddress(reversedAddress);
            String myIP = myInetIP.getHostAddress();

            return myIP;
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }

        return null;
    }

    public static boolean isWifiConnected(Context context)
    {
        boolean isConnnected = false;

        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();

            if(netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.isConnected())
            {
                isConnnected = true;
            }
            else
            {
                isConnnected = false;
            }
        }catch (Exception e)
        {
            ConsoleLog.e(e);
        }


        return isConnnected;
    }
}
