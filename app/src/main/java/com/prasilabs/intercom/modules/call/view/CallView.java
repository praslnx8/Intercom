package com.prasilabs.intercom.modules.call.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.audioserver.Mic;
import com.prasilabs.intercom.audioserver.Speaker;
import com.prasilabs.intercom.constants.CommonConstant;
import com.prasilabs.intercom.core.CoreApp;
import com.prasilabs.intercom.customs.JsonUtil;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.managers.UserManager;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.utils.ViewUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by prasi on 1/9/16.
 */
public class CallView
{
    private static final String TAG = CallView.class.getSimpleName();

    private static boolean isInCall = false;

    public static void showCallView(Context context, final UserInfo userInfo)
    {
        isInCall = true;

        final View callView = View.inflate(context, R.layout.layout_accept_call, null);

        ImageView userImage = (ImageView) callView.findViewById(R.id.user_image);
        TextView nameText = (TextView) callView.findViewById(R.id.name_text);
        Button endBtn = (Button) callView.findViewById(R.id.end_btn);

        final LinearLayout acceptLayout = (LinearLayout) callView.findViewById(R.id.accept_layout);
        final LinearLayout endLayout = (LinearLayout) callView.findViewById(R.id.end_layout);

        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                // Shrink the window to wrap the content rather than filling the screen
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                // Display it on top of other application windows, but only for the current user
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                // Don't let it grab the input focus
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                // Make the underlying application window visible through any transparent parts
                PixelFormat.TRANSLUCENT);

// Define the position of the window within the screen
        p.gravity = Gravity.FILL;

        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(callView, p);


        ViewUtil.renderImage(userImage, userInfo.getPic(), true);
        nameText.setText(ViewUtil.formatAsName(userInfo.getName()));

        acceptLayout.setVisibility(View.GONE);
        endLayout.setVisibility(View.VISIBLE);

        final Mic mic = new Mic(userInfo.getIp());
        final Speaker speaker = new Speaker();

        final DataOutputStream[] dataOutputStream = new DataOutputStream[1];
        final DataInputStream[] dataInputStream = new DataInputStream[1];
        final Socket[] socket = new Socket[1];


        new Thread()
        {
            @Override
            public void run()
            {

                try
                {
                    socket[0] = new Socket(userInfo.getIp(), CommonConstant.MAIN_PORT);

                    if(socket[0].isConnected())
                    {
                        dataOutputStream[0] = new DataOutputStream(socket[0].getOutputStream());
                        dataInputStream[0] = new DataInputStream(socket[0].getInputStream());

                        dataOutputStream[0].writeUTF("callingu");
                        dataOutputStream[0].writeUTF(String.valueOf(JsonUtil.getJsonFromClass(UserManager.getMyInfo(CoreApp.getAppContext()))));

                        String reply = dataInputStream[0].readUTF();

                        ConsoleLog.i(TAG, "msg is : " + reply);

                        if(reply.equalsIgnoreCase("busy"))
                        {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    ViewUtil.t(CoreApp.getAppContext(), "user is busy");
                                }
                            });
                            dataInputStream[0].close();
                            dataOutputStream[0].close();
                            socket[0].close();
                        }
                        else
                        {
                            speaker.start();

                            mic.start();
                        }
                    }
                    else
                    {
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                ViewUtil.t(CoreApp.getAppContext(), "It seeems user went offline :( ");
                            }
                        });
                        socket[0].close();
                    }
                }catch (Exception e)
                {
                    ConsoleLog.e(e);

                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            ViewUtil.t(CoreApp.getAppContext(), "It seems user went offline");
                        }
                    });

                    windowManager.removeViewImmediate(callView);
                }
            }
        }.start();

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    mic.interrupt();
                    speaker.interrupt();
                    dataInputStream[0].close();
                    dataOutputStream[0].close();
                    socket[0].close();

                    windowManager.removeViewImmediate(callView);

                }catch (Exception e)
                {
                    ConsoleLog.e(e);
                }
            }
        });
    }

    public static void showIncominCallView(final Context context, final UserInfo userInfo, final CallListener callListener)
    {
        new Handler(Looper.myLooper()).post(new Runnable() {
            @Override
            public void run()
            {
                isInCall = true;

                final View callView = View.inflate(context, R.layout.layout_accept_call, null);

                ImageView userImage = (ImageView) callView.findViewById(R.id.user_image);
                TextView nameText = (TextView) callView.findViewById(R.id.name_text);
                Button acceptBtn = (Button) callView.findViewById(R.id.accept_btn);
                Button rejectBtn = (Button) callView.findViewById(R.id.reject_btn);
                Button endBtn = (Button) callView.findViewById(R.id.end_btn);

                final LinearLayout acceptLayout = (LinearLayout) callView.findViewById(R.id.accept_layout);
                final LinearLayout endLayout = (LinearLayout) callView.findViewById(R.id.end_layout);

                WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                        // Shrink the window to wrap the content rather than filling the screen
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        // Display it on top of other application windows, but only for the current user
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        // Don't let it grab the input focus
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        // Make the underlying application window visible through any transparent parts
                        PixelFormat.TRANSLUCENT);

// Define the position of the window within the screen
                p.gravity = Gravity.TOP | Gravity.END;
                p.x = 0;
                p.y = 100;

                final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.addView(callView, p);

                ViewUtil.renderImage(userImage, userInfo.getPic(), true);
                nameText.setText(ViewUtil.formatAsName(userInfo.getName()));

                acceptLayout.setVisibility(View.VISIBLE);
                endLayout.setVisibility(View.GONE);

                final Mic mic = new Mic(userInfo.getIp());
                final Speaker speaker = new Speaker();

                acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        isInCall = true;

                        mic.start();
                        speaker.start();

                        acceptLayout.setVisibility(View.GONE);
                        endLayout.setVisibility(View.VISIBLE);
                    }
                });

                rejectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        windowManager.removeViewImmediate(callView);
                        isInCall = false;
                    }
                });

                endBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        windowManager.removeViewImmediate(callView);
                        isInCall = false;

                        if(callListener != null)
                        {
                            callListener.ended();
                        }
                    }
                });
            }
        });

    }

    public static boolean isInCall() {
        return isInCall;
    }

    public interface CallListener
    {
        void ended();
    }
}
