package com.prasilabs.intercom.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.customs.CircleTransform;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;


/**
 * Created by prasi on 26/5/16.
 */
public class ViewUtil
{
    private static final String TAG = ViewUtil.class.getSimpleName();

    public static void ts(Context context, String message)
    {
        if(context != null && !TextUtils.isEmpty(message))
        {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void t(Context context, String message)
    {
        if(context != null && !TextUtils.isEmpty(message))
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void hideProgressView(Context context, ViewGroup viewGroup)
    {
        showProgressView(context, viewGroup, false, false);
    }

    public static void showProgressView(Context context, ViewGroup viewGroup, boolean isBig)
    {
        showProgressView(context, viewGroup, isBig, true);
    }

    private static void showProgressView(Context context, ViewGroup viewGroup, boolean isBig, boolean isShow)
    {
        if(isShow)
        {
            View view = View.inflate(context, R.layout.widget_progress_dialog, null);

            ProgressBar bigProgressBar = (ProgressBar) view.findViewById(R.id.big_progress_bar);
            ProgressBar smallProgressBar = (ProgressBar) view.findViewById(R.id.small_progress_bar);

            if(isBig)
            {
                bigProgressBar.setVisibility(View.VISIBLE);
                smallProgressBar.setVisibility(View.GONE);
            }
            else
            {
                bigProgressBar.setVisibility(View.GONE);
                smallProgressBar.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                viewGroup.getChildAt(i).setVisibility(View.GONE);
            }
            viewGroup.addView(view);
        }
        else
        {
            if(viewGroup.getChildCount() > 0)
            {
                if(viewGroup.getChildCount() > 1)
                {
                    for(int i= 0; i<viewGroup.getChildCount() -1; i++)
                    {
                        viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }

                viewGroup.removeView(viewGroup.getChildAt(viewGroup.getChildCount() - 1));
            }
        }
    }

    public static void renderImage(ImageView view, String url, boolean isCircle)
    {
        if(!TextUtils.isEmpty(url))
        {
            ConsoleLog.i(TAG, "picture url is : " + url);
            try
            {
                RequestCreator requestCreator = Picasso.with(view.getContext()).load(url).placeholder(android.R.drawable.ic_menu_camera);
                if(isCircle)
                {
                    requestCreator.transform(new CircleTransform());
                }
                requestCreator.into(view);
            }
            catch (Exception e)
            {
                ConsoleLog.e(e);
            }
        }
    }

    public static String formatAsName(String s)
    {
        //TODO
        return s;
    }

    public static String toTitleCase(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        } else {
            return "";
        }
    }

    public static String toUpperCase(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text.toUpperCase();
        } else {
            return "";
        }
    }

    public static String toLowerCase(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text.toLowerCase();
        } else {
            return "";
        }
    }

    public static String toCamelCase(String text) {
        if (!TextUtils.isEmpty(text)) {
            String[] words = text.split(" ");
            StringBuilder camelText = new StringBuilder();
            for (String word : words) {
                camelText.append(toTitleCase(word));
                camelText.append(" ");
            }
            return camelText.toString().trim();
        } else {
            return "";
        }
    }

    public static void hideKeyboard(View view, Context context)
    {
        ConsoleLog.i(TAG, "hiding keyboard");
        try
        {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (Exception e)
        {
            ConsoleLog.e(e);
        }
    }

    public static void openKeyBoard(View view, Context context)
    {
        try
        {
            InputMethodManager inputMethodManager=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
        catch (Exception e)
        {
            ConsoleLog.e(e);
        }
    }

    /*public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        if(count > 0)
        {
            badge.setAlpha(255);
        }
        else
        {
            badge.setAlpha(0);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }*/

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
