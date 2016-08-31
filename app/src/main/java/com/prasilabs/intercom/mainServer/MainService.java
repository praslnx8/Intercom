package com.prasilabs.intercom.mainServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.prasilabs.intercom.constants.CommonConstant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainService extends Service
{
    public MainService()
    {

    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            ServerSocket serverSocket = new ServerSocket(CommonConstant.MAIN_PORT);

            while (true)
            {
                try
                {
                    Socket socket = serverSocket.accept();


                    DataInputStream is = new DataInputStream(socket.getInputStream());

                    while (socket.isConnected())
                    {
                        String message = is.readUTF();
                        if(message.equalsIgnoreCase("whoareu"))
                        {
                            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

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

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
