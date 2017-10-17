package com.ctid.intelsmsapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import com.ctid.intelsmsapp.utils.LogUtil;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.adapter.DetailMessageAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        //使用Intent对象得到短信会话的id
        Intent intent = getIntent();
        threadId = intent.getStringExtra("threadId");
        detailMessagesListView = (ListView)this.findViewById(R.id.detailMessageListView);
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

}
