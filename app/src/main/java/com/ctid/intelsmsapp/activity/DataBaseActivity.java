package com.ctid.intelsmsapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ctid.intelsmsapp.database.DataBaseHelper;
import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.bean.UserInfo;

import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/11
 * @email sunzhiwei@ctid.com.cn
 */
public class DataBaseActivity extends DialogEnabledActivity {
    private EditText tvName;
    private EditText tvNumber;
    private TextView tvShow;
    private Button btnSave;
    private Button btnQuery;
    private DataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_activity);
        dbHelper = DataBaseHelper.getDefaultDBHelper(this);
        tvName = (EditText)findViewById(R.id.tv1);
        tvNumber = (EditText)findViewById(R.id.tv2);
        tvShow = (TextView)findViewById(R.id.tv3);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnQuery = (Button)findViewById(R.id.btn_query);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insertSql(tvName.getText().toString(), tvNumber.getText().toString(), null);
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UserInfo> userInfoList = dbHelper.querySql();
                StringBuffer stringBuffer = new StringBuffer();
                if(userInfoList != null && userInfoList.size() > 0){
                    for(int i = 0; i < userInfoList.size(); i++){
                        stringBuffer.append(i + " .userName=" + userInfoList.get(i).getUserName()
                        + "  userNumber=" + userInfoList.get(i).getUserNumber() + "\n");
                    }
                    tvShow.setText(stringBuffer);
                }
            }
        });
    }
}
