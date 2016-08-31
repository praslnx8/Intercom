/*
package com.prasilabs.intercom.services.network.volley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medaho.userapp.core.CoreApp;
import com.medaho.userapp.debug.ConsoleLog;
import com.medaho.userapp.enums.ErrorCode;
import com.medaho.vendor.services.network.NetworkManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

*/
/**
 * Created by prasi on 11/7/16.
 *//*

public class VolleySingleton
{
    public static final int GET = Request.Method.GET;
    public static final int POST = Request.Method.POST;
    public static final int PUT = Request.Method.PUT;

    private static final String TAG = VolleySingleton.class.getSimpleName();

    private static final RequestQueue mRequestQueue = Volley.newRequestQueue(CoreApp.getAppContext());
    private static final ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
    DefaultRetryPolicy requestPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private static final VolleySingleton instance = new VolleySingleton();

    public static VolleySingleton getInstance() {
        return instance;
    }

    protected  <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        mRequestQueue.add(req);
    }

    protected  <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(false);
        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        mRequestQueue.cancelAll(tag);
    }

    public void makeStringRequest(final String url, final JSONObject jsonObject, final VolleyCallBack volleyCallBack)
    {
        if(new NetworkManager(CoreApp.getAppContext(), null).isOnline())
        {
            ConsoleLog.i(TAG, "making api call : " + url);
            ConsoleLog.i(TAG, "with content : " + jsonObject.toString());
            StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    ConsoleLog.i(TAG, "result for " + url);
                    ConsoleLog.i(TAG, response);
                    volleyCallBack.result(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ConsoleLog.e(error);
                    if (volleyCallBack != null) {
                        volleyCallBack.error(ErrorCode.GENERAL);
                    }
                }
            })
            {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonObject.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                //In your extended request class
                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                        volleyError = error;
                    }

                    return volleyError;
                }
            };

            stringRequest.setRetryPolicy(requestPolicy);
            addToRequestQueue(stringRequest);
        }
    }

    public void makeStringRequest(int mode, String url, final Map<String, String> mParams, final VolleyCallBack volleyCallBack)
    {
        if(new NetworkManager(CoreApp.getAppContext(), null).isOnline())
        {
            if(mode == Request.Method.POST || mode == Request.Method.PUT)
            {
                ConsoleLog.i(TAG, mode + " dmaking api call  : " + url);
                ConsoleLog.i(TAG, "with params : " + constructUrl("", mParams));
                StringRequest stringRequest = new StringRequest(mode, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        volleyCallBack.result(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        ConsoleLog.i(TAG, error.getMessage());
                        ConsoleLog.e(error);
                        if (volleyCallBack != null) {
                            volleyCallBack.error(ErrorCode.GENERAL);
                        }
                    }
                })
                {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
                    }

                    @Override
                    protected Map<String, String> getParams()
                    {
                        return mParams;
                    }
                };

                stringRequest.setRetryPolicy(requestPolicy);
                addToRequestQueue(stringRequest);
            }
            else
            {
                String appendedURl = constructUrl(url, mParams);
                ConsoleLog.i(TAG, "making api call : " + appendedURl);
                StringRequest stringRequest = new StringRequest(mode, appendedURl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        ConsoleLog.i(TAG, "response is : " + response);
                        volleyCallBack.result(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ConsoleLog.e(error);
                        if (volleyCallBack != null)
                        {
                            volleyCallBack.error(ErrorCode.GENERAL);
                        }
                    }
                });

                stringRequest.setRetryPolicy(requestPolicy);
                addToRequestQueue(stringRequest);
            }


        }
        else
        {
            if(volleyCallBack != null)
            {
                volleyCallBack.error(ErrorCode.NOT_CONNECTED);
            }
        }
    }

    public static String constructUrl(String url, Map<String, String> mParams)
    {
        StringBuilder builder = new StringBuilder();

        if(mParams != null)
        {
            for (String key : mParams.keySet())
            {
                String value = String.valueOf(mParams.get(key));
                if (value != null) {
                    try {
                        value = URLEncoder.encode(String.valueOf(value), "UTF-8");


                        if (builder.length() > 0)
                            builder.append("&");
                        builder.append(key).append("=").append(value);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

            if (url.contains("?"))
            {
                url += "&" + builder.toString();
            }
            else {
                url += "?" + builder.toString();
            }
        }

        ConsoleLog.i(TAG, "Built URL " + url);
        return url;
    }



    public interface VolleyCallBack
    {
        void result(String result);

        void error(ErrorCode errorCode);
    }

}
*/
