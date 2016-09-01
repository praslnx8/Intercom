package com.prasilabs.intercom.modules.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.audioserver.Mic;
import com.prasilabs.intercom.audioserver.Speaker;
import com.prasilabs.intercom.constants.CommonConstant;
import com.prasilabs.intercom.customs.JsonUtil;
import com.prasilabs.intercom.debug.ConsoleLog;
import com.prasilabs.intercom.managers.UserManager;
import com.prasilabs.intercom.modules.call.view.CallView;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.utils.ViewUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 1/9/16.
 */
public class IpListAdapter extends BaseAdapter
{
    private List<UserInfo> userInfoList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public IpListAdapter(Context context)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void cearAndAddUsers(List<UserInfo> userInfoList)
    {
        if(userInfoList != null && userInfoList.size() > 0)
        {
            this.userInfoList.clear();
            this.userInfoList.addAll(userInfoList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount()
    {
        return userInfoList.size() + 1;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        if(userInfoList.size() > i)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {

        if(userInfoList.size() > i)
        {
            view = layoutInflater.inflate(R.layout.item_user_info, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.user_image);
            TextView nameText = (TextView) view.findViewById(R.id.name_text);
            TextView callText = (TextView) view.findViewById(R.id.call_btn);

            final UserInfo userInfo = userInfoList.get(i);

            ViewUtil.renderImage(imageView, userInfo.getPic(), true);
            nameText.setText(ViewUtil.formatAsName(userInfo.getName()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        final Socket socket = new Socket(userInfo.getIp(), CommonConstant.MAIN_PORT);

                        if(socket.isConnected())
                        {
                            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                            dataOutputStream.writeUTF("callingu");
                            dataOutputStream.writeUTF(String.valueOf(JsonUtil.getJsonFromClass(UserManager.getMyInfo(context))));

                            String reply = dataInputStream.readUTF();

                            if(reply.equalsIgnoreCase("busy"))
                            {
                                ViewUtil.t(context, "user is busy");
                                dataInputStream.close();
                                dataOutputStream.close();
                                socket.close();
                            }
                            else
                            {
                                final Speaker speaker = new Speaker();
                                speaker.start();

                                final Mic mic = new Mic(userInfo.getIp());
                                mic.start();

                                CallView.showIncominCallView(context, userInfo, false, new CallView.CallListener() {
                                    @Override
                                    public void ended()
                                    {
                                        try
                                        {
                                            mic.interrupt();
                                            speaker.interrupt();
                                            dataInputStream.close();
                                            dataOutputStream.close();
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
                        else
                        {
                            ViewUtil.t(context, "It seeems user went offline :( ");
                            socket.close();
                        }
                    }catch (Exception e)
                    {
                        ConsoleLog.e(e);

                        ViewUtil.t(context, "It seems user went offline");
                    }
                }
            });
        }
        else
        {
            view = layoutInflater.inflate(R.layout.item_share_app, null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO share
                }
            });
        }

        return view;
    }
}
