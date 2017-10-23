package com.ctid.intelsmsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.MessageListAdapter;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.enums.SysConstants;
import com.ctid.intelsmsapp.net.ISynDataService;
import com.ctid.intelsmsapp.net.impl.SynDataServiceImpl;
import com.ctid.intelsmsapp.utils.DBUtil;
import com.ctid.intelsmsapp.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DialogEnabledActivity {

    private ListView messageListView;
    private Button newMessageButton;
    private MessageListAdapter adapter;
    private Context mContext;
    List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate");
        setContentView(R.layout.activity_main);
        newMessageButton = (Button) findViewById(R.id.newMessageButton);
        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DataBaseActivity.class);
                startActivity(intent);
            }
        });

        //获取用于显示短信会话列表的ListView控件
        messageListView = (ListView) this.findViewById(R.id.messageListView);

        mContext = this;
        new InitDBTask().execute();
    }

    private void initData() {
        ISynDataService synDataService = new SynDataServiceImpl(this);
        try {
            synDataService.autoSysPlatDataToLocalDB(SysConstants.DATA_SYN_TYPE_IN);
        } catch (Exception e) {
            LogUtil.e(e.toString(), e);
        }
    }

    private class InitDBTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog(getString(R.string.loading));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                initData();
            } catch (Exception e) {
                LogUtil.e(e.toString(), e);
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            LogUtil.d("InitDBTask onPostExecute");
            new InitMessageListTask().execute();
        }
    }

    private class InitMessageListTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //为控件加载需要显示的数据
                messageInfoList = DBUtil.getMessageSessions(mContext);
                adapter = new MessageListAdapter(mContext, messageInfoList);
            } catch (Exception e) {
                LogUtil.e(e.toString(), e);
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            LogUtil.d("InitMessageListTask onPostExecute");
            //新建并为ListView设置自定义适配器，为控件加载需要显示的数据
            messageListView.setAdapter(adapter);
            dismissLoadingDialog();
        }
    }

}
