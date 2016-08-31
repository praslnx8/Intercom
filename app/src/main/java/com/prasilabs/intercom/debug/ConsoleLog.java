package com.prasilabs.intercom.debug;

import android.util.Log;

import com.prasilabs.intercom.core.CoreApp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Created by prasi on 11/3/16.
 */
public class ConsoleLog
{
    public static void i(String tag, String message)
    {
        if(CoreApp.appDebug)
        {
            Logger.getLogger(tag).info(message);
        }
    }

    public static void w(String tag, String message)
    {
        if(CoreApp.appDebug)
        {
            Logger.getLogger(tag).warning(message);
        }
    }

    public static void s(String tag, String message)
    {
        if(CoreApp.appDebug)
        {
            Logger.getLogger(tag).severe(message);
        }
    }

    public static void t(Throwable throwable)
    {
        if(CoreApp.appDebug)
        {
            throwable.printStackTrace();
        }
    }
    public static void e(Exception e)
    {
        if(CoreApp.appDebug)
        {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Log.e("EXCEPTION", errors.toString());
        }
    }
}
