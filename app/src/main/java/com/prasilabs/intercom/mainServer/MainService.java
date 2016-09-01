package com.prasilabs.intercom.mainServer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service
{
    public static void startMainService(Context context)
    {
        Intent intent = new Intent(context, MainService.class);
        context.startService(intent);
    }

    public MainService()
    {

    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        MainServer mainServer = new MainServer(this);

        mainServer.start();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
