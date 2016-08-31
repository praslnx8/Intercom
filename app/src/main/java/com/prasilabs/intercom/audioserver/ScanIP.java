package com.prasilabs.intercom.audioserver;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ScanIP extends Thread
{
    int f1i;
    String[] iplist;
    String subnet;
    Context context;

    public ScanIP(Context context)
    {
        this.f1i = 0;
        this.context = context;
    }

    public void run() {
        getSubnet();
    }

    void getSubnet() {
        String local = getLocalIP(context); //TODO
        System.out.println(local);
        String[] ip_component = local.split("\\.");
        this.subnet = ip_component[0] + "." + ip_component[1] + "." + ip_component[2] + ".";
        this.f1i = 1;
        while (this.f1i <= 254) {
            String addr = this.subnet + this.f1i;
            try {
                System.out.println("Trying " + addr);
                addConnection(InetAddress.getByName(addr).isReachable(500), addr);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.f1i++;
        }
    }

    void addConnection(boolean in, String addr) {
        if (in) {
            System.out.println("Found" + addr);
        }
    }

    private String getLocalIP(Context context)
    {
        String local = Formatter.formatIpAddress(((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
        if (local.equals("0.0.0.0")) {
            local = "192.168.1.1";
        }

        return local;
    }
}