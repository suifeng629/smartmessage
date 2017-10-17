package com.ctid.intelsmsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.MessageListAdapter;
import com.ctid.intelsmsapp.entity.Company;
import com.ctid.intelsmsapp.entity.Menu;
import com.ctid.intelsmsapp.utils.LogUtil;

import java.util.List;
import java.util.Random;

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

        dataExists();
        insertNewCompany();

        int flag = 7;
        if (flag == 7) {
            //获取用于显示短信会话列表的ListView控件
            messageListView = (ListView) this.findViewById(R.id.messageListView);
            //新建并为ListView设置自定义适配器，为控件加载需要显示的数据
            MessageListAdapter adapter = new MessageListAdapter(this);

            adapter.getMessageSessions();
            messageListView.setAdapter(adapter);
            //实时通知数据已更新
            //adapter.notifyDataSetChanged();
        }
    }

    boolean dataExists() {
        List<Company> companies = Company.listAll(Company.class);
        for (Company company : companies) {
            LogUtil.d(company.toString());
        }
        return !companies.isEmpty();
    }

    void insertNewCompany() {
        Random random = new Random();
        Company company = new Company();
        company.setIcon("123");
        company.setNumber("12809i09i");
        company.setNumId(new Random().nextInt());
        company.setTitle("Test company");

        for(int i = 0; i < 2; i++) {
            Menu menu = new Menu();
            menu.setMenuId(new Random().nextInt());
        }


        company.save();
        LogUtil.d("Company inserted");
    }
}
