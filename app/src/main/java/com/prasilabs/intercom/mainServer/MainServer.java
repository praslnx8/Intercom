package com.prasilabs.intercom.mainServer;

import android.content.Context;

import com.prasilabs.intercom.constants.CommonConstant;
import com.prasilabs.intercom.customs.JsonUtil;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.managers.UserManager;
import com.prasilabs.intercom.modelEngines.UsersModelEngine;
import com.prasilabs.intercom.modules.call.view.CallView;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.services.network.IntercomWifiManager;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by prasi on 1/9/16.
 */
public class MainServer extends Thread
{
    private Context context;

    public MainServer(Context context)
    {
        this.context = context;
    }

    @Override
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(CommonConstant.MAIN_PORT);

            while (true)
            {
                try
                {
                    final Socket socket = serverSocket.accept();

                    String ipAddr = socket.getInetAddress().toString(); //TODO
                    UsersModelEngine.getInstance().scanUser(ipAddr);

                    final DataInputStream is = new DataInputStream(socket.getInputStream());
                    final DataOutputStream os = new DataOutputStream(socket.getOutputStream());

                    while (socket.isConnected())
                    {
                        String message = is.readUTF();
                        if(message.equalsIgnoreCase("whoareu"))
                        {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setIp(IntercomWifiManager.getWifiIp(context));
                            userInfo.setName(UserManager.getName(context));
                            userInfo.setPic(UserManager.getPicture(context));

                            JSONObject jsonObject = JsonUtil.getJsonFromClass(userInfo);
                            if(jsonObject != null)
                            {
                                os.writeUTF(String.valueOf(jsonObject));
                            }
                            else
                            {
                                os.writeUTF("");
                            }

                            socket.close();
                        }
                        else if(message.equalsIgnoreCase("callingu"))
                        {
                            String userInfoStr = is.readUTF();

                            UserInfo userInfo = JsonUtil.getObjectFromJson(userInfoStr, UserInfo.class);

                            if(CallView.isInCall())
                            {
                                os.writeUTF("busy");
                            }
                            else
                            {
                                os.writeUTF("ready");
                                if(userInfo != null)
                                {
                                    CallView.showIncominCallView(context, userInfo, new CallView.CallListener() {
                                        @Override
                                        public void ended()
                                        {
                                            try
                                            {
                                                is.close();
                                                os.close();
                                                socket.close();
                                            }
                                            catch (Exception e)
                                            {
                                                ConsoleLog.e(e);
                                            }
                                        }
                                    });

                                }
                            }
                        }
                        else
                        {
                            is.close();
                            os.close();
                            socket.close();
                        }
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
