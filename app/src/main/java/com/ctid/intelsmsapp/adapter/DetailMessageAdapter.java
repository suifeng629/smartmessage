package com.ctid.intelsmsapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.bean.MessageHolder;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.entity.Model;
import com.ctid.intelsmsapp.enums.SysConstants;
import com.ctid.intelsmsapp.utils.LogUtil;
import com.ctid.sms.SmsParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<Model> models = new ArrayList<Model>();

    //DetailMessageAdapter初始化构造方法
    public DetailMessageAdapter(Context context, List<MessageInfo> infoList, List<Model> modelList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        infos = infoList;
        models = modelList;
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
                //匹配模板显示模板UI，不匹配显示默认
                if (models != null && models.size() > 0) {
                    String smsbody = infos.get(position).getSmsbody();
                    boolean matcherFlag = false;
                    String modelType = null;
                    Map<String, String> modelMap = new HashMap<String, String>();
                    for (Model model : models) {
                        if (SmsParse.isMatcher(smsbody, model.getReg(), Integer.valueOf(model.getRegGroup()))) {
                            modelMap = SmsParse.getModeMap(smsbody, model.getReg(), model.getRegCfg());
                            LogUtil.d(modelMap.toString());
                            matcherFlag = true;
                            modelType = model.getRegCfg();
                        }
                    }
                    if (matcherFlag) {//匹配布局
                        if (modelType.contains(SysConstants.MODLE_TYPE_CODE)) {
                            //验证码
                            convertView = mInflater.inflate(R.layout.code_layout, null);
                            ((TextView) convertView.findViewById(R.id.time)).setText(infos.get(position).getDate());
                            //设置验证码code_1
                            ((TextView) convertView.findViewById(R.id.code_1)).setText(modelMap.get("code_1"));
                            //设置短信内容
                            ((TextView) convertView.findViewById(R.id.code_text)).setText(infos.get(position).getSmsbody());

                        } else if (modelType.contains(SysConstants.MODLE_TYPE_TRAIN)) {
                            //火车票
                            convertView = mInflater.inflate(R.layout.train_layout, null);
                            ((TextView) convertView.findViewById(R.id.time)).setText(infos.get(position).getDate());

                            ((TextView) convertView.findViewById(R.id.train_1)).setText(modelMap.get("train_1"));
                            ((TextView) convertView.findViewById(R.id.train_2)).setText(modelMap.get("train_2"));
                            ((TextView) convertView.findViewById(R.id.train_3)).setText(modelMap.get("train_3"));
                            ((TextView) convertView.findViewById(R.id.train_4)).setText(modelMap.get("train_4"));
                            ((TextView) convertView.findViewById(R.id.train_5)).setText(modelMap.get("train_5"));

                        } else if (modelType.contains(SysConstants.MODLE_TYPE_BANK)) {
                            //银行
                            convertView = mInflater.inflate(R.layout.bank_layout, null);
                            ((TextView) convertView.findViewById(R.id.time)).setText(infos.get(position).getDate());
                            if(!TextUtils.isEmpty(infos.get(position).getContactName())){
                                ((TextView) convertView.findViewById(R.id.banktitle)).setText(infos.get(position).getContactName());
                            }

                            ((TextView) convertView.findViewById(R.id.bank_1)).setText(modelMap.get("bank_1"));
                            ((TextView) convertView.findViewById(R.id.bank_2)).setText("**********" + modelMap.get("bank_2"));
                            ((TextView) convertView.findViewById(R.id.bank_3)).setText(modelMap.get("bank_3"));
                            ((TextView) convertView.findViewById(R.id.bank_4)).setText(modelMap.get("bank_4"));
                            ((TextView) convertView.findViewById(R.id.bank_5)).setText(modelMap.get("bank_5"));

                        } else {
                            convertView = mInflater.inflate(R.layout.detail_message_list_received_item, null);
                            receivedMessageHolder = new MessageHolder();
                            receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(
                                    R.id.ReceivedDetailMessageTextView));
                            receivedMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                        }
                    } else {//不匹配，夹在默认布局
                        convertView = mInflater.inflate(R.layout.detail_message_list_received_item, null);
                        receivedMessageHolder = new MessageHolder();
                        receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(
                                R.id.ReceivedDetailMessageTextView));
                        receivedMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                    }
                } else {
                    //为收到的信息关联格式文件，设置显示格式
                    convertView = mInflater.inflate(R.layout.detail_message_list_received_item, null);
                    receivedMessageHolder = new MessageHolder();
                    receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(
                            R.id.ReceivedDetailMessageTextView));

                    receivedMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                }
                break;
            case 2:
                //为发出的信息关联格式文件，设置显示格式
                convertView = mInflater.inflate(R.layout.detail_message_list_send_item, null);
                sendMessageHolder = new MessageHolder();
                sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                        R.id.SendMessageTextView));
                sendMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                break;
            //若从sms表提取的信息type为其他，说明这是发出的信息
            default:
                //为发出的信息关联格式文件，设置显示格式
                convertView = mInflater.inflate(R.layout.detail_message_list_send_item, null);
                sendMessageHolder = new MessageHolder();
                sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                        R.id.SendMessageTextView));
                sendMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                break;
        }
        return convertView;
    }
}
