package com.ctid.intelsmsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.MessageListAdapter;
import com.ctid.intelsmsapp.net.ISynDataService;
import com.ctid.intelsmsapp.net.impl.SynDataServiceImpl;
import com.ctid.intelsmsapp.utils.LogUtil;

public class MainActivity extends DialogEnabledActivity {

    private ListView messageListView;
    private Button newMessageButton;


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
        //新建并为ListView设置自定义适配器，为控件加载需要显示的数据
        MessageListAdapter adapter = new MessageListAdapter(this);

        adapter.getMessageSessions();
        messageListView.setAdapter(adapter);
        //实时通知数据已更新
        //adapter.notifyDataSetChanged();

        initData();
    }

    private void initData() {
        ISynDataService synDataService = new SynDataServiceImpl(this);
        try {
            synDataService.autoSysPlatDataToLocalDB(3);
        } catch (Exception e) {
            LogUtil.e(e.toString(), e);
        }
    }

}
