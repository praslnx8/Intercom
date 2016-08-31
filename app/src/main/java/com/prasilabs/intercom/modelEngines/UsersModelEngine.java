package com.prasilabs.intercom.modelEngines;

import android.os.AsyncTask;

import com.prasilabs.intercom.constants.CommonConstant;
import com.prasilabs.intercom.core.CoreApp;
import com.prasilabs.intercom.core.CoreModelEngine;
import com.prasilabs.intercom.customs.JsonUtil;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.services.network.IntercomWifiManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 31/8/16.
 */
public class UsersModelEngine extends CoreModelEngine
{
    private List<UserInfo> userInfoList = new ArrayList<>();

    private static final UsersModelEngine instance = new UsersModelEngine();

    public static UsersModelEngine getInstance()
    {
        return instance;
    }

    public void scanUsers(final ScanUserCallBack scanUserCallBack)
    {
        new AsyncTask<Void, Integer, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                userInfoList.clear();

                String local = IntercomWifiManager.getWifiIp(CoreApp.getAppContext());
                System.out.println(local);
                String[] ip_component = local.split("\\.");
                String subnet = ip_component[0] + "." + ip_component[1] + "." + ip_component[2] + ".";
                for(int i=1;i<255; i++)
                {
                    String addr = subnet + i;
                    try
                    {
                        publishProgress(i);

                        System.out.println("Trying " + addr);
                        if(!addr.equalsIgnoreCase(local))
                        {
                            if(InetAddress.getByName(addr).isReachable(200))
                            {
                                Socket clientSocket = new Socket(addr, CommonConstant.MAIN_PORT);

                                if(clientSocket.isConnected())
                                {
                                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                                    DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                                    dataOutputStream.writeUTF("whoareu");
                                    String data = dataInputStream.readUTF();

                                    UserInfo userInfo = JsonUtil.getObjectFromJson(data, UserInfo.class);

                                    if(userInfo != null && userInfo.getName() != null)
                                    {
                                        userInfoList.add(userInfo);

                                        publishProgress(i, userInfoList.size());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        ConsoleLog.e(e);
                    }
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                super.onProgressUpdate(values);

                int ipCount = 0;
                int userCount = 0;
                if(values.length > 0)
                {
                    ipCount = values[0];
                }
                if(values.length > 1)
                {
                    userCount = values[1];
                }

                if(scanUserCallBack != null)
                {
                    scanUserCallBack.scaned(ipCount, userCount);
                }
            }
        }.execute();
    }

    public interface ScanUserCallBack
    {
        void scaned(int ipCount, int userCount);

        void usersFound(List<UserInfo> userInfos);
    }
}
