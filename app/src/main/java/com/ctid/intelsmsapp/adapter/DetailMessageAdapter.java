package com.ctid.intelsmsapp.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.bean.MessageHolder;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.utils.LogUtil;

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

    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";

    private static final String GetMessagesByThreadIdTAG = "Getting messages by thread id";

    //存储信息会话中所有来往短信的列表
    List<MessageInfo> infos = new ArrayList<MessageInfo>();


    //DetailMessageAdapter初始化构造方法
    public DetailMessageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * @param thread_id 根据thread_id 检索sms库， 获得该会话包含的信息
     * @return
     */
    public void getDetailMessages(String thread_id) {
        Cursor detailMessagesCursor = null;
        ContentResolver resolver = null;

        /**
         获取短信的各种信息 ，短信数据库sms表结构如下：
         _id：短信序号，如100　　
         　 　 thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的            　　
         　  　address：发件人地址，即手机号，如+8613811810000            　　
         　  　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null            　　
         　  　date：日期，long型，如1256539465022，可以对日期显示格式进行设置            　　
         　  　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信            　　
         　  　read：是否阅读0未读，1已读            　　
         　  　status：短信状态-1接收，0complete,64pending,128failed            　　
         　  　type：短信类型1是接收到的，2是已发出            　　
         　  　body：短信具体内容            　　
         　  　service_center：短信服务中心号码编号，如+8613800755500
         */
        try {
            String[] projection = new String[]
                    {"thread_id", "address", "person", "body", "date", "type", "read"};
            Uri uri = Uri.parse(SMS_URI_ALL);
            resolver = mContext.getContentResolver();

            detailMessagesCursor = resolver.query
                    (
                            uri,
                            projection,
                            "thread_id=?",
                            new String[]{thread_id},
                            "date asc"
                    );

            if (detailMessagesCursor == null) {
                return;
            }
            if (detailMessagesCursor.getCount() == 0) {
                detailMessagesCursor.close();
                detailMessagesCursor = null;
                return;
            }

            detailMessagesCursor.moveToFirst();
            while (detailMessagesCursor.isAfterLast() == false) {
                int nameColumn = detailMessagesCursor.getColumnIndex("person");
                int phoneNumberColumn = detailMessagesCursor.getColumnIndex("address");
                int smsbodyColumn = detailMessagesCursor.getColumnIndex("body");
                int dateColumn = detailMessagesCursor.getColumnIndex("date");
                int typeColumn = detailMessagesCursor.getColumnIndex("type");

                MessageInfo smsinfo = new MessageInfo();
                //将信息会话的信息内容和信息类型（收到或发出）存入infos中
                smsinfo.setSmsbody(detailMessagesCursor.getString(smsbodyColumn));
                smsinfo.setType(detailMessagesCursor.getString(typeColumn));
                infos.add(smsinfo);
                detailMessagesCursor.moveToNext();
            }
            if (infos != null && infos.size() > 0) {
                int count_1 = 0;
                int count_2 = 0;
                for (int i = 0; i < infos.size(); i++) {
                    if (Integer.valueOf(infos.get(i).getType()) == 1) {
                        count_1++;
                    } else if (Integer.valueOf(infos.get(i).getType()) == 2) {
                        count_2++;
                        LogUtil.d("sunzhiwei--smsbody=" + infos.get(i).getSmsbody());
                    }
                }
                LogUtil.d("sunzhiwei--count_1=" + count_1 + "  count_2=" + count_2 + "  infos.size=" + infos.size());
            }

        } catch (Exception e) {
            LogUtil.e(GetMessagesByThreadIdTAG + e.toString());
        } finally {
            if (detailMessagesCursor != null) {
                detailMessagesCursor.close();
                detailMessagesCursor = null;
            }
        }
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
