package com.prasilabs.intercom.modules.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prasilabs.intercom.R;
import com.prasilabs.intercom.pojo.UserInfo;
import com.prasilabs.intercom.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasi on 1/9/16.
 */
public class IpListAdapter extends BaseAdapter
{
    private List<UserInfo> userInfoList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public IpListAdapter(Context context)
    {
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

            UserInfo userInfo = userInfoList.get(i);

            ViewUtil.renderImage(imageView, userInfo.getPic(), true);
            nameText.setText(ViewUtil.formatAsName(userInfo.getName()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //TODO make call

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
