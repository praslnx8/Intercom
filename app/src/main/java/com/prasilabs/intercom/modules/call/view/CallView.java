package com.prasilabs.intercom.modules.call.view;

import android.content.Context;
import android.graphics.PixelFormat;
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
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.utils.ViewUtil;

/**
 * Created by prasi on 1/9/16.
 */
public class CallView
{
    private static boolean isInCall = false;

    public static void showIncominCallView(Context context, final UserInfo userInfo, boolean isIncoming, final CallListener callListener)
    {
        isInCall = true;

        View view = View.inflate(context, R.layout.layout_accept_call, null);

        ImageView userImage = (ImageView) view.findViewById(R.id.user_image);
        TextView nameText = (TextView) view.findViewById(R.id.name_text);
        Button acceptBtn = (Button) view.findViewById(R.id.accept_btn);
        Button rejectBtn = (Button) view.findViewById(R.id.reject_btn);
        Button endBtn = (Button) view.findViewById(R.id.end_btn);

        final LinearLayout acceptLayout = (LinearLayout) view.findViewById(R.id.accept_layout);
        final LinearLayout endLayout = (LinearLayout) view.findViewById(R.id.end_layout);

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

        ViewUtil.renderImage(userImage, userInfo.getPic(), true);
        nameText.setText(ViewUtil.formatAsName(userInfo.getName()));

        if(isIncoming)
        {
            acceptLayout.setVisibility(View.VISIBLE);
            endLayout.setVisibility(View.GONE);
        }
        else
        {
            acceptLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.VISIBLE);
        }

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
                windowManager.removeViewImmediate(view);
                isInCall = false;

                mic.interrupt();
                speaker.interrupt();

                if(callListener != null)
                {
                    callListener.ended();
                }
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                windowManager.removeViewImmediate(view);
                isInCall = false;

                if(callListener != null)
                {
                    callListener.ended();
                }
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
