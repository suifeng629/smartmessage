package com.ctid.intelsmsapp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.entity.Company;
import com.ctid.intelsmsapp.enums.SysConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/18
 * @email sunzhiwei@ctid.com.cn
 */
public class DBUtil {
    /**
     * 从mms数据库中检索threads表
     */
    public static List<MessageInfo> getMessageSessions(Context mContext) {
        Cursor cursor = null;
        ContentResolver resolver = null;
        //存储所有短信信息的列表
        List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();

        try {
            Uri uri = Uri.parse(SysConstants.SMS_URI_ALL);
            resolver = mContext.getContentResolver();
            cursor = resolver.query(uri, new String[]
                    {"* from threads--"}, null, null, null);

            if (cursor == null) {
                return null;
            }
            if (cursor.getCount() == 0) {
                cursor.close();
                cursor = null;
                return null;
            }

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {  /*
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

                String phoneAndUnread[] = getPhoneNum(mContext, threadId);
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
                List<Company> companyList = Company.find(Company.class, "number = ?", phoneAndUnread[0]);
                if (companyList != null && companyList.size() > 0) {
                    Company company = companyList.get(0);
                    if (company != null && company.getTitle() != null) {
                        smsinfo.setContactName(company.getTitle());
                        smsinfo.setContactMes(company.getTitle());
                        smsinfo.setPhoneUrl(company.getIcon());
                    } else {
                        smsinfo.setContactPhoto(BitmapFactory.decodeResource(
                                mContext.getResources(), R.drawable.icon));
                        smsinfo.setContactMes(phone);
                    }
                } else {
                    smsinfo.setContactPhoto(BitmapFactory.decodeResource(
                            mContext.getResources(), R.drawable.icon));
                    smsinfo.setContactMes(phone);
                }

                //获得会话的未读短信与所有短信数
                String final_count = unreadCount + "/" + count_mms;
                smsinfo.setContactNumber(phone);
                smsinfo.setDate(date_mms);
                smsinfo.setSmsbody(last_mms);
                smsinfo.setType(type);
                smsinfo.setMessageCout(final_count);
                smsinfo.setThreadId(threadId);
                messageInfoList.add(smsinfo);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            LogUtil.e("Reading from Thread Table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return messageInfoList;

    }

    /**
     * @param thread_id 根据thread_id 检索sms库， 获得对应的号码以及该号码的未读信息数
     * @return
     */
    public static String[] getPhoneNum(Context mContext, String thread_id) {
        Cursor cursor = null;
        String PhoneNum = "";
        int noread_mms = 0;
        String[] phoneAndUnread = {"", ""};

        try {
            String[] projection = new String[]
                    {"thread_id", "address", "person", "body", "date", "type", "read"};

            //SMS_URI_ALL = "content://sms/";
            Uri uri = Uri.parse(SysConstants.SMS_URI_ALL);
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
            LogUtil.e("Getting phone number" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return phoneAndUnread;
    }

    /**
     * 短信详情列表查询
     *
     * @param thread_id 根据thread_id 检索sms库， 获得该会话包含的信息
     * @return
     */
    public static List<MessageInfo> getDetailMessages(Context context, String thread_id) {
        Cursor detailMessagesCursor = null;
        ContentResolver resolver = null;
        //存储信息会话中所有来往短信的列表
        List<MessageInfo> infos = new ArrayList<MessageInfo>();

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
            Uri uri = Uri.parse(SysConstants.SMS_URI_ALL);
            resolver = context.getContentResolver();

            detailMessagesCursor = resolver.query
                    (
                            uri,
                            projection,
                            "thread_id=?",
                            new String[]{thread_id},
                            "date asc"
                    );

            if (detailMessagesCursor == null) {
                return null;
            }
            if (detailMessagesCursor.getCount() == 0) {
                detailMessagesCursor.close();
                detailMessagesCursor = null;
                return null;
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

        } catch (Exception e) {
            LogUtil.e("Getting messages by thread id" + e.toString());
        } finally {
            if (detailMessagesCursor != null) {
                detailMessagesCursor.close();
                detailMessagesCursor = null;
            }
        }
        return infos;
    }
}
