package com.ctid.intelsmsapp.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ctid.intelsmsapp.activity.DetailMessageActivity;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.database.DataBaseHelper;
import com.ctid.intelsmsapp.utils.LogUtil;
import com.ctid.myandroiddemos.R;
import com.ctid.intelsmsapp.bean.MessageHolder;
import com.ctid.intelsmsapp.bean.UserInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/10
 * @email sunzhiwei@ctid.com.cn
 */
public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;

    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 已发送短信
     */
    public static final String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public static final String SMS_URI_DRAFT = "content://sms/draft";

    private static final String MesTAG = "Reading Messages";
    private static final String ThreadTAG = "Reading from Thread Table";
    private static final String GetPhoneNumberTAG = "Getting phone number";
    private static final String GetContactByPhoneTAG = "Getting contact by phone number";

    //存储所有短信信息的列表
    List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();

    private DataBaseHelper dbHelper;


    //MessageListAdapter初始化构造方法
    public MessageListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        dbHelper = DataBaseHelper.getDefaultDBHelper(context);
    }


    /**
     * 从mms数据库中检索threads表
     */
    public void getMessageSessions() {
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[]{"* from threads--"};
        messageListQueryHandler = new MessageListQueryHandler(mContext.getContentResolver());
        messageListQueryHandler.startQuery(0, null, uri, projection, null, null, null);

    }

    private MessageListQueryHandler messageListQueryHandler;

    public class MessageListQueryHandler extends AsyncQueryHandler {

        public MessageListQueryHandler(ContentResolver cr) {
            super(cr);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            // TODO Auto-generated method stub
            super.onQueryComplete(token, cookie, cursor);
            UserInfo userInfo = null;
            ContentResolver resolver = mContext.getContentResolver();

            if (cursor == null) {
                return;
            }
            if (cursor.getCount() == 0) {
                cursor.close();
                cursor = null;
                return;
            }

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                /*
                threads表各字段含义如下：
                1._id为会话id，他关联到sms表中的thread_id字段。
                2.recipient_ids为联系人ID,这个ID不是联系人表中的_id,而是指向表canonical_address里的id,
                canonical_address这个表同样位于mmssms.db,它映射了recipient_ids到一个电话号码,也就是说,
                最终获取联系人信息,还是得通过电话号码;
                3.mesage_count该会话的消息数量
                4.snippet为最后收到或发出的信息
                */

                int thread_idColumn = cursor.getColumnIndex("_id");
                int dateColumn = cursor.getColumnIndex("date");
                int message_countColumn = cursor.getColumnIndex("message_count");
                int snippetColumn = cursor.getColumnIndex("snippet");
                int typeColumn = cursor.getColumnIndex("type");

                //格式化短信日期显示
                Date date = new Date(Long.parseLong(cursor.getString(dateColumn)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                //获得短信的各项内容
                //phoneAndUnread[0]存放电话号码，phoneAndUnread[1]存放该会话中未读信息数
                String threadId = cursor.getString(thread_idColumn);

                String phoneAndUnread[] = getPhoneNum(threadId);
                String phone = phoneAndUnread[0];
                String unreadCount = phoneAndUnread[1];
                String last_mms = cursor.getString(snippetColumn);
                String date_mms = dateFormat.format(date);
                String count_mms = cursor.getString(message_countColumn);
                String type = cursor.getString(typeColumn);

                MessageInfo smsinfo = new MessageInfo();

                /*
                phoneAndUnread[0]存放电话号码
                根据号码查询本地db，获取商户信息（商户名称，图标等）
                */
                userInfo = dbHelper.queryByNumberSql(phoneAndUnread[0]);

                //获得会话的未读短信与所有短信数
                String final_count = unreadCount + "/" + count_mms;

                smsinfo.setContactNumber(phone);
                if (userInfo != null) {
                    if (!TextUtils.isEmpty(userInfo.getUserName())) {
                        smsinfo.setContactName(userInfo.getUserName());
                        smsinfo.setContactMes(userInfo.getUserName());
                    } else {
                        //smsinfo.setContactName(mContext.getResources().getString(R.string.unkown));
                        smsinfo.setContactMes(phone);
                    }
                    //如果有该信息会话人头像，则设置已有头像，如果没有则给他设置一个默认的头像
                    if (!TextUtils.isEmpty(userInfo.getUserIcon())) {
                        //smsinfo.setContactName(userInfo.getUserName());
                    } else {
                        smsinfo.setContactPhoto(BitmapFactory.decodeResource(
                                mContext.getResources(), R.drawable.icon));
                    }
                    /*if (contact.getPhotoUri() == null){
                        smsinfo.setContactPhoto(BitmapFactory.decodeResource(
                                mContext.getResources(), R.drawable.icon));
                    }else{
                        Uri photoUri = contact.getPhotoUri();
                        InputStream input = ContactsContract.Contacts.
                                openContactPhotoInputStream(resolver, photoUri);
                        smsinfo.setContactPhoto(BitmapFactory.decodeStream(input));
                    }*/
                } else {
                    smsinfo.setContactPhoto(BitmapFactory.decodeResource(
                            mContext.getResources(), R.drawable.icon));
                    //smsinfo.setContactName(mContext.getResources().getString(R.string.unkown));
                    smsinfo.setContactMes(phone);
                }


                smsinfo.setDate(date_mms);
                smsinfo.setSmsbody(last_mms);
                smsinfo.setType(type);
                smsinfo.setMessageCout(final_count);
                smsinfo.setThreadId(threadId);
                messageInfoList.add(smsinfo);
                cursor.moveToNext();
            }
            cursor.close();
        }
    }


    /**
     * @param thread_id 根据thread_id 检索sms库， 获得对应的号码以及该号码的未读信息数
     * @return
     */
    public String[] getPhoneNum(String thread_id) {
        Cursor cursor = null;
        String PhoneNum = "";
        int noread_mms = 0;
        String[] phoneAndUnread = {"", ""};

        try {
            String[] projection = new String[]
                    {"thread_id", "address", "person", "body", "date", "type", "read"};

            //SMS_URI_ALL = "content://sms/";
            Uri uri = Uri.parse(SMS_URI_ALL);
            ContentResolver resolver = mContext.getContentResolver();

            cursor = resolver.query
                    (
                            uri,
                            projection,
                            "thread_id=?",
                            new String[]{thread_id},
                            null
                    );

            //计算该会话包含的未读短信数
            while (cursor.moveToNext()) {
                int phoneNumber = cursor.getColumnIndex("address");
                int isread = cursor.getColumnIndex("read");

                //sms表的read字段为0，表示该短信为未读短信
                if (cursor.getString(isread).equals("0")) {
                    noread_mms++;
                }

                PhoneNum = cursor.getString(phoneNumber);
            }
            phoneAndUnread[0] = PhoneNum;
            phoneAndUnread[1] = Integer.toString(noread_mms);

            cursor.close();
            cursor = null;
        } catch (Exception e) {
            LogUtil.e(GetPhoneNumberTAG + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return phoneAndUnread;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return messageInfoList.size();
    }

    public View getView(final int position, View convertView, android.view.ViewGroup parent) {
        MessageHolder messageHolder = null;

        //判断convertView是否已创建，若已存在则不必重新创建新视图，节省系统资源
        if (convertView == null) {
            // 和item_custom.xml脚本关联
            convertView = mInflater.inflate(R.layout.list_item, null);

            messageHolder = new MessageHolder();

            //加载控件到信息载体中
            messageHolder.setIvImage((ImageView) convertView.findViewById(R.id.contact_image));
            messageHolder.setTvTitle((TextView) convertView.findViewById(R.id.titleTextView));
            messageHolder.setTvDesc((TextView) convertView.findViewById(R.id.descTextView));
            messageHolder.setTvCount((TextView) convertView.findViewById(R.id.countTextView));
            messageHolder.setTvTime((TextView) convertView.findViewById(R.id.timeTextView));

            //将联系人信息载体Holder放入convertView视图
            convertView.setTag(messageHolder);

        } else {
            //从convertView视图取出联系人信息载体Holder
            messageHolder = (MessageHolder) convertView.getTag();
        }

        // 通过短信Holder设置item中4个TextView的文本与联系人头像
        //绘制短信联系人信息，内容为其名称或号码
        messageHolder.getTvTitle().setText(messageInfoList.get(position).getContactMes());
        //在信息正文区域绘制信息内容
        messageHolder.getTvDesc().setText(messageInfoList.get(position).getSmsbody());

        messageHolder.getTvCount().setText("" + messageInfoList.get(position).getMessageCout());
        messageHolder.getTvTime().setText(messageInfoList.get(position).getDate());
        messageHolder.getIvImage().setImageBitmap(messageInfoList.get(position).getContactPhoto());

        /*
         * 在短信主界面为每个短信会话设置监听事件，当选择点击某条会话时，跳转到显示该会话包含的所有信息记录的页面
         * */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //通过Intent向显示短信会话包含的信息的Activity传递会话id
                intent.putExtra("threadId", messageInfoList.get(position).getThreadId());
                intent.setClass(mContext, DetailMessageActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
