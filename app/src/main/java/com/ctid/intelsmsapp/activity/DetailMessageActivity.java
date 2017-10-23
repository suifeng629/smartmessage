package com.ctid.intelsmsapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.ChildMenuListAdapter;
import com.ctid.intelsmsapp.adapter.DetailMessageAdapter;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.ctid.intelsmsapp.entity.Company;
import com.ctid.intelsmsapp.entity.Menu;
import com.ctid.intelsmsapp.entity.Model;
import com.ctid.intelsmsapp.utils.DBUtil;
import com.ctid.intelsmsapp.utils.FuncString;
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
    private ImageView back;
    private TextView companyTitle;
    List<Menu> menuList = new ArrayList<Menu>();
    List<Menu> childMenuList1 = new ArrayList<Menu>();
    List<Menu> childMenuList2 = new ArrayList<Menu>();
    List<Menu> childMenuList3 = new ArrayList<Menu>();
    List<Company> companyList;
    private Context mContext;
    private String childLocationFlag = null;

    private PopupWindow pop;
    private ChildMenuListAdapter childMenuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        mContext = this;
        //匹配号码显示的底部菜单布局
        ll_menu = (LinearLayout) findViewById(R.id.id_bottom_menu_layout);
        //不匹配好吗显示的底部输入框菜单
        ll_input = (LinearLayout) findViewById(R.id.id_bottom_input_layout);
        //使用Intent对象得到短信会话的id
        Intent intent = getIntent();
        threadId = intent.getStringExtra("threadId");
        number = intent.getStringExtra("number");

        detailMessagesListView = (ListView) this.findViewById(R.id.detailMessageListView);

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
                LogUtil.d("sunzhiwei----" + FuncString.getPhoneNumber(number));
                //商户号码判断底部菜单布局,不为空说明有底部菜单
                menuList = Menu.find(Menu.class, "number = ?", FuncString.getPhoneNumber(number));
                companyList = Company.find(Company.class, "number = ?", FuncString.getPhoneNumber(number));
                LogUtil.d("sunzhiwei---infos start");
                List<MessageInfo> infos = DBUtil.getDetailMessages(mContext, threadId);
                LogUtil.d("sunzhiwei---infos end");
                List<Model> models = Model.find(Model.class, "number = ?", FuncString.getPhoneNumber(number));
                detailMessagesAdapter = new DetailMessageAdapter(mContext, infos, models);
                LogUtil.d("sunzhiwei---detailMessagesAdapter");
            } catch (Exception e) {
                LogUtil.e(e.toString(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            LogUtil.d("onPostExecute");
            if (menuList != null && menuList.size() > 0) {
                ll_menu.setVisibility(View.VISIBLE);
                ll_input.setVisibility(View.GONE);
            } else {
                ll_menu.setVisibility(View.GONE);
                ll_input.setVisibility(View.VISIBLE);
            }
            //初始化菜单并设置菜单事件
            initBottomMenu();
            clickMenu();
            if (companyList != null && companyList.size() > 0) {
                companyTitle.setText(companyList.get(0).getTitle());
            } else {
                companyTitle.setText(number);
            }

            detailMessagesListView.setAdapter(detailMessagesAdapter);
            //实时通知数据已更新
            detailMessagesAdapter.notifyDataSetChanged();
            dismissLoadingDialog();
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
        back = (ImageView) findViewById(R.id.back);
        companyTitle = (TextView) findViewById(R.id.company_title);

        if (ll_menu != null && ll_menu.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < menuList.size(); i++) {
                if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 1) {
                    tv1.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if (!TextUtils.isEmpty(url)) {
                        tv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    } else {
                        //子菜单判断
                        for (int j = 0; j < menuList.size(); j++) {
                            if (menuList.get(j).getMenuParent() == menuList.get(i).getMenuId()
                                    && menuList.get(j).getMenuLevel() == 2) {
                                childMenuList1.add(menuList.get(j));
                            }
                        }
                        //子菜单不为空，加载子菜单布局，最左侧
                        if (childMenuList1 != null && childMenuList1.size() > 0) {
                            LogUtil.d("childMenuList1--" + childMenuList1.size());
                            childMenuListAdapter = new ChildMenuListAdapter(this, childMenuList1);
                            menu1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    childLocationFlag = "left";
                                    showChildMenus(childMenuList1, childLocationFlag);
                                }
                            });

                        }
                    }

                } else if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 2) {
                    tv2.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if (!TextUtils.isEmpty(url)) {
                        tv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    } else {
                        //子菜单判断
                        for (int j = 0; j < menuList.size(); j++) {
                            if (menuList.get(j).getMenuParent() == menuList.get(i).getMenuId()
                                    && menuList.get(j).getMenuLevel() == 2) {
                                childMenuList2.add(menuList.get(j));
                            }
                        }
                        //子菜单不为空，加载子菜单布局，中间
                        if (childMenuList2 != null && childMenuList2.size() > 0) {
                            LogUtil.d("childMenuList2--" + childMenuList2.size());
                            childMenuListAdapter = new ChildMenuListAdapter(this, childMenuList2);
                            menu2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    childLocationFlag = "middle";
                                    showChildMenus(childMenuList2, childLocationFlag);
                                }
                            });
                        }
                    }
                } else if (menuList.get(i).getMenuLevel() == 1 && menuList.get(i).getMenuSort() == 3) {
                    tv3.setText(menuList.get(i).getMenuName());
                    final String url = menuList.get(i).getMenuUrl();
                    if (!TextUtils.isEmpty(url)) {
                        tv3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openURL(getApplicationContext(), url);
                            }
                        });
                    } else {
                        //子菜单判断
                        for (int j = 0; j < menuList.size(); j++) {
                            if (menuList.get(j).getMenuParent() == menuList.get(i).getMenuId()
                                    && menuList.get(j).getMenuLevel() == 2) {
                                childMenuList3.add(menuList.get(j));
                            }
                        }
                        //子菜单不为空，加载子菜单布局，最右侧
                        if (childMenuList3 != null && childMenuList3.size() > 0) {
                            LogUtil.d("childMenuList3--" + childMenuList3.size());

                            menu3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    childMenuListAdapter = new ChildMenuListAdapter(mContext, childMenuList3);
                                    childLocationFlag = "right";
                                    showChildMenus(childMenuList3, childLocationFlag);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void clickMenu() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        input_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input_edittext.setFocusableInTouchMode(true);
            }
        });
    }

    // 点击标题显示子菜单
    private void showChildMenus(List<Menu> childMenuList, String flag) {
        pop = createMeasuredPop(childMenuList);
        if (pop != null) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            if ("left".equals(flag)) {
                pop.showAtLocation(ll_menu, Gravity.LEFT | Gravity.BOTTOM, 0,
                        getResources().getDimensionPixelSize(R.dimen.model_title_height));
            }
            if ("middle".equals(flag)) {
                pop.showAtLocation(ll_menu, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0,
                        getResources().getDimensionPixelSize(R.dimen.model_title_height));
            }
            if ("right".equals(flag)) {
                pop.showAtLocation(ll_menu, Gravity.RIGHT | Gravity.BOTTOM, 0,
                        getResources().getDimensionPixelSize(R.dimen.model_title_height));
            }
        }

    }

    private PopupWindow createMeasuredPop(List<Menu> childMenuList) {
        childMenuListAdapter = new ChildMenuListAdapter(mContext, childMenuList);
        if (childMenuListAdapter == null || childMenuListAdapter.getCount() == 0) {
            return null;
        }

        View view = View.inflate(this, R.layout.child_menu_list, null);
        ListView typeInfoListView = (ListView) view.findViewById(R.id.title_list);
        typeInfoListView.setAdapter(childMenuListAdapter);
        typeInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                String url = ((Menu) childMenuListAdapter.getItem(position)).getMenuUrl();
                openURL(mContext, url);

                if (pop.isShowing()) {
                    pop.dismiss();
                }
            }
        });

        int totalHeight = 0;
        int maxWidth = 0;
        for (int i = 0; i < childMenuListAdapter.getCount(); i++) {
            View itemView = childMenuListAdapter.getView(i, null, null);
            itemView.measure(0, 0);
            totalHeight += itemView.getMeasuredHeight();
            int width = itemView.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        PopupWindow popupWindow = new PopupWindow(view, maxWidth * 2, totalHeight);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return popupWindow;
    }

    /**
     * 跳转url
     */
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
