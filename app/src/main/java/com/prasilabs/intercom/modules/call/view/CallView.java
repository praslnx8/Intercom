package com.prasilabs.intercom.modules.call.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.pojo.UserInfo;

/**
 * Created by prasi on 1/9/16.
 */
public class CallView
{
    private static boolean isInCall = false;

    public static void showIncominCallView(Context context, UserInfo userInfo)
    {
        isInCall = true;

        View view = View.inflate(context, R.layout.layout_accept_call, null);

        Button acceptBtn = (Button) view.findViewById(R.id.accept_btn);
        Button rejectBtn = (Button) view.findViewById(R.id.reject_btn);

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
        windowManager.addView(view, p);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                isInCall = true;
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                windowManager.removeViewImmediate(view);
                isInCall = false;
            }
        });
    }

    public static boolean isInCall() {
        return isInCall;
    }
}
