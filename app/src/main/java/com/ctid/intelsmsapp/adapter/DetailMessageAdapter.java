package com.ctid.intelsmsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.bean.MessageHolder;
import com.ctid.intelsmsapp.bean.MessageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/10
 * @email sunzhiwei@ctid.com.cn
 */
public class DetailMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;
    //存储信息会话中所有来往短信的列表
    List<MessageInfo> infos = new ArrayList<MessageInfo>();

    //DetailMessageAdapter初始化构造方法
    public DetailMessageAdapter(Context context ,List<MessageInfo> infoList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        infos = infoList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder receivedMessageHolder = null;
        MessageHolder sendMessageHolder = null;
        switch (Integer.parseInt(infos.get(position).getType())) {
            //若从sms表提取的信息type为1，说明这是收到的信息
            case 1:
                //为收到的信息关联格式文件，设置显示格式
                convertView = mInflater.inflate(R.layout.detail_message_list_received_item, null);
                receivedMessageHolder = new MessageHolder();
                receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(
                        R.id.ReceivedDetailMessageTextView));
                break;
            case 2:
                //为发出的信息关联格式文件，设置显示格式
                convertView = mInflater.inflate(R.layout.detail_message_list_send_item, null);
                sendMessageHolder = new MessageHolder();
                sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                        R.id.SendMessageTextView));
                break;
            //若从sms表提取的信息type为其他，说明这是发出的信息
            default:
                //为发出的信息关联格式文件，设置显示格式
                convertView = mInflater.inflate(R.layout.detail_message_list_send_item, null);
                sendMessageHolder = new MessageHolder();
                sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                        R.id.SendMessageTextView));
                break;
        }

        //设置资源
        switch (Integer.parseInt(infos.get(position).getType())) {
            case 1:
                receivedMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                break;
            default:
                sendMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                break;
        }
        return convertView;
    }
}
