package com.prasilabs.intercom.modelEngines;

import android.content.Intent;
import android.os.AsyncTask;

import com.prasilabs.intercom.constants.BroadCastConstant;
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
    private static final String TAG = UsersModelEngine.class.getSimpleName();

    private List<UserInfo> userInfoList = new ArrayList<>();
    private int ipCount = 0;

    private static final UsersModelEngine instance = new UsersModelEngine();

    public static UsersModelEngine getInstance()
    {
        return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void scanUser(String ip)
    {
        new AsyncTask<String,Void,Void>()
        {
            @Override
            protected Void doInBackground(String... ips)
            {

                String ip = ips[0];

                try {
                    if(ip != null)
                    {
                        if (InetAddress.getByName(ip).isReachable(200))
                        {
                            ConsoleLog.i(TAG, "found " + ip);
                            Socket clientSocket = new Socket(ip, CommonConstant.MAIN_PORT);

                            if (clientSocket.isConnected()) {
                                ConsoleLog.i(TAG, "connected to ip : " + ip);
                                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                                dataOutputStream.writeUTF("whoareu");
                                String data = dataInputStream.readUTF();

                                UserInfo userInfo = JsonUtil.getObjectFromJson(data, UserInfo.class);

                                if (userInfo != null && userInfo.getName() != null)
                                {
                                    userInfoList.add(userInfo);

                                    Intent intent = new Intent();
                                    intent.setAction(BroadCastConstant.USER_CHANGE_REFRESH);
                                    sendBroadCast(intent);
                                }
                            }
                        }
                    }
                }catch (Exception e)
                {
                    ConsoleLog.e(e);
                }

                return null;
            }
        }.execute(ip);
    }

    public void scanUsers(final ScanUserCallBack scanUserCallBack)
    {
        userInfoList.clear();
        ipCount = 0;

        final boolean[] isThreadOneFinished = {false};
        final boolean[] isThreadTwoFinished = {false};
        final boolean[] isThreadThreeFinished = {false};
        final boolean[] isThreadFourFinished = {false};

        ScanAsyncCallBack scanAsyncCallBack = new ScanAsyncCallBack() {
            @Override
            public void scanCount()
            {
                if(scanUserCallBack != null)
                {
                    scanUserCallBack.scaned(ipCount, userInfoList.size());
                }
            }

            @Override
            public void finished(int thread)
            {
                if(thread == 1)
                {
                    isThreadOneFinished[0] = true;
                }
                else if(thread == 2)
                {
                    isThreadTwoFinished[0] = true;
                }
                else if(thread == 3)
                {
                    isThreadThreeFinished[0] = true;
                }
                else if(thread == 4)
                {
                    isThreadFourFinished[0] = true;
                }

                if(isThreadOneFinished[0] && isThreadTwoFinished[0] && isThreadThreeFinished[0] && isThreadFourFinished[0])
                {
                    if(scanUserCallBack != null)
                    {
                        scanUserCallBack.usersFound(userInfoList);
                    }
                }
            }
        };

        ScanAsyncTask scanAsyncTask1 = new ScanAsyncTask(scanAsyncCallBack, 1);
        ScanAsyncTask scanAsyncTask2 = new ScanAsyncTask(scanAsyncCallBack, 2);
        ScanAsyncTask scanAsyncTask3 = new ScanAsyncTask(scanAsyncCallBack, 3);
        ScanAsyncTask scanAsyncTask4 = new ScanAsyncTask(scanAsyncCallBack, 4);

        scanAsyncTask1.execute();
        scanAsyncTask2.execute();
        scanAsyncTask3.execute();
        scanAsyncTask4.execute();
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public interface ScanUserCallBack
    {
        void scaned(int ipCount, int userCount);

        void usersFound(List<UserInfo> userInfos);
    }


    private class ScanAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        private ScanAsyncCallBack scanAsyncCallBack;
        private int thread;

        public ScanAsyncTask(ScanAsyncCallBack scanAsyncCallBack, int thread)
        {
            this.scanAsyncCallBack = scanAsyncCallBack;
            this.thread = thread;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String local = IntercomWifiManager.getWifiIp(CoreApp.getAppContext());
            if(local != null)
            {
                ConsoleLog.i(TAG, "truing 255 ips of ip list " + local);
                String[] ip_component = local.split("\\.");
                String subnet = ip_component[0] + "." + ip_component[1] + "." + ip_component[2] + ".";

                int start = 0;
                int end = 0;
                if(thread == 1)
                {
                    start = 1;
                    end = 64;
                }
                else if(thread == 2)
                {
                    start = 65;
                    end = 124;
                }
                else if(thread == 3)
                {
                    start = 125;
                    end = 190;
                }
                else if(thread == 4)
                {
                    start = 191;
                    end = 254;
                }

                for (int i = start; i <= end; i++)
                {
                    String addr = subnet + i;
                    try
                    {
                        if (!addr.equalsIgnoreCase(local))
                        {
                            if (InetAddress.getByName(addr).isReachable(200))
                            {
                                ConsoleLog.i(TAG, "found " + addr);
                                Socket clientSocket = new Socket(addr, CommonConstant.MAIN_PORT);

                                if (clientSocket.isConnected())
                                {
                                    ConsoleLog.i(TAG, "connected to ip : " + addr);
                                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                                    DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                                    dataOutputStream.writeUTF("whoareu");
                                    String data = dataInputStream.readUTF();

                                    UserInfo userInfo = JsonUtil.getObjectFromJson(data, UserInfo.class);

                                    if (userInfo != null && userInfo.getName() != null)
                                    {
                                        userInfoList.add(userInfo);
                                    }
                                }
                            }
                        }

                        ipCount++;
                        publishProgress(ipCount);

                    } catch (Exception e) {
                        ConsoleLog.e(e);
                    }
                }
            }
            else
            {
                ConsoleLog.i(TAG, "interuppted");
                cancel(true);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            int ipCount = 0;
            if(values.length > 0)
            {
                ipCount = values[0];
            }

            if(scanAsyncCallBack != null)
            {
                scanAsyncCallBack.scanCount();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(scanAsyncCallBack != null)
            {
                scanAsyncCallBack.finished(thread);
            }
        }
    }

    public interface ScanAsyncCallBack
    {
        void scanCount();

        void finished(int thread);
    }
}
