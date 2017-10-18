package com.ctid.intelsmsapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.DetailMessageAdapter;
import com.ctid.intelsmsapp.utils.LogUtil;
import com.ctid.intelsmsapp.entity.Menu;

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
public class DetailMessageActivity extends DialogEnabledActivity {
    private ListView detailMessagesListView;
    DetailMessageAdapter detailMessagesAdapter;
    String threadId;
    String number;//商户号码
    private LinearLayout ll_menu;
    private LinearLayout ll_input;
    private EditText input_edittext;
    private RelativeLayout menu1;
    private RelativeLayout menu2;
    private RelativeLayout menu3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    List<com.ctid.intelsmsapp.entity.Menu> menuList = new ArrayList<com.ctid.intelsmsapp.entity.Menu>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        //匹配号码显示的底部菜单布局
        ll_menu = (LinearLayout) findViewById(R.id.id_bottom_menu_layout);
        //不匹配好吗显示的底部输入框菜单
        ll_input = (LinearLayout) findViewById(R.id.id_bottom_input_layout);
        //使用Intent对象得到短信会话的id
        Intent intent = getIntent();
        threadId = intent.getStringExtra("threadId");
        number = intent.getStringExtra("number");
        //商户号码判断底部菜单布局
        for(int i = 0; i < 3; i++){
            Menu menu = new Menu();
            menu.setMenuLevel(1);
            menu.setMenuName("菜单" + i);
            menu.setMenuSort(i + 1);
            menuList.add(menu);
        }
        if ("14".equals(threadId) && menuList != null && menuList.size() > 0) {
            ll_menu.setVisibility(View.VISIBLE);
            ll_input.setVisibility(View.GONE);
        } else {
            ll_menu.setVisibility(View.GONE);
            ll_input.setVisibility(View.VISIBLE);
        }
        //初始化菜单并设置菜单事件
        initBottomMenu();
        clickMenu();

        detailMessagesListView = (ListView) this.findViewById(R.id.detailMessageListView);
        detailMessagesAdapter = new DetailMessageAdapter(this);
        new LoadingMessageTask().execute();

    }

    private class LoadingMessageTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog(getString(R.string.loading));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                detailMessagesAdapter.getDetailMessages(threadId);
            } catch (Exception e) {
                LogUtil.e(e.toString(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            LogUtil.d("onPostExecute");
            detailMessagesListView.setAdapter(detailMessagesAdapter);
            //实时通知数据已更新
            detailMessagesAdapter.notifyDataSetChanged();
            if (result == null) {
                dismissLoadingDialog();

            } else {

                dismissLoadingDialog();
            }

        }
    }

    private void initBottomMenu() {
        input_edittext = (EditText) findViewById(R.id.input_text);
        menu1 = (RelativeLayout) findViewById(R.id.btn1);
        menu2 = (RelativeLayout) findViewById(R.id.btn2);
        menu3 = (RelativeLayout) findViewById(R.id.btn3);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);
        tv3 = (TextView) findViewById(R.id.text3);
        if (ll_menu != null && ll_menu.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < menuList.size(); i++) {
                if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 1) {
                    tv1.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if(!TextUtils.isEmpty(url)){
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    }
                    //子菜单判断
                    for(int j = 0; j < menuList.size(); j++){
                        if(menuList.get(j).getMenuParent() == menuList.get(i).getMenuId()
                                && menuList.get(j).getMenuLevel() == 2){

                        }
                    }
                } else if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 2) {
                    tv2.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if(!TextUtils.isEmpty(url)){
                        tv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    }
                } else if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 3) {
                    tv3.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if(!TextUtils.isEmpty(url)){
                        tv3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    }
                }
            }
        }
    }

    private void clickMenu() {
        input_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input_edittext.setFocusableInTouchMode(true);
            }
        });
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 跳转url
     * */
    public static final void openURL(Context context, String url) {
        launchIntent(context, new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static final void launchIntent(Context context, Intent intent) {
        if (intent != null) {
            try {
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.s_msg_intent_failed);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        }
    }

}
