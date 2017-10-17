package com.ctid.intelsmsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.ctid.intelsmsapp.bean.UserInfo;
import com.ctid.intelsmsapp.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/11
 * @email sunzhiwei@ctid.com.cn
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final String DATABASE_TABLE = "userinfos";
    private static final int DATABASE_VERSION = 1;
    private static DataBaseHelper dbHelper;

    private SQLiteDatabase sqLiteDatabase;

    public static DataBaseHelper getDefaultDBHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new DataBaseHelper(context);
        }
        return dbHelper;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.sqLiteDatabase = db;
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.sqLiteDatabase = db;
        if (newVersion != oldVersion) {
            initTables();
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if (sqLiteDatabase != null) {
            db = sqLiteDatabase;
        } else {
            db = super.getWritableDatabase();
        }
        return db;
    }

    /**
     * 初始化数据库表
     */
    public void initTables() {
        this.sqLiteDatabase = getWritableDatabase();
        this.sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                DATABASE_TABLE);
        createTables(this.sqLiteDatabase);
    }

    /**
     * 创建数据库表
     */
    private void createTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " ("
                + "_ID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userName" + " TEXT,"
                + "userNumber" + " TEXT,"
                + "userIcon" + " TEXT"
                + ");");
    }

    /**
     * 插入
     */
    public long insertSql(String userName, String userNumber, String userIcon) {
        this.sqLiteDatabase = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("userName", userName);
        initialValues.put("userNumber", userNumber);
        initialValues.put("userIcon", userIcon);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * 查询
     */
    public List<UserInfo> querySql() {
        Cursor cursor = null;
        List<UserInfo> userInfoList = null;
        try {
            userInfoList = new ArrayList<UserInfo>();
            this.sqLiteDatabase = getWritableDatabase();
            cursor = this.sqLiteDatabase.query(DATABASE_TABLE,
                    null, null, null, null, null, null);
            int numRows = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < numRows; i++) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(cursor.getString(1));
                userInfo.setUserNumber(cursor.getString(2));
                userInfo.setUserIcon(cursor.getString(3));
                userInfoList.add(userInfo);
                cursor.moveToNext();
            }
            return userInfoList;
        } catch (Exception e) {
            LogUtil.e("DataBaseHelper-querySql:" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userInfoList;

    }

    /**
     * 根据手机号码查询商户信息
     */
    public UserInfo queryByNumberSql(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return null;
        }
        Cursor cursor = null;
        UserInfo userInfo = null;
        try {
            this.sqLiteDatabase = getWritableDatabase();
            cursor = this.sqLiteDatabase.query(DATABASE_TABLE,
                    null, null, null, null, null, null);
            int numRows = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < numRows; i++) {
                if (phoneNumber.equals(cursor.getString(2))) {
                    userInfo = new UserInfo();
                    userInfo.setUserName(cursor.getString(1));
                    userInfo.setUserNumber(cursor.getString(2));
                    userInfo.setUserIcon(cursor.getString(3));
                    break;
                }
                cursor.moveToNext();
            }
            return userInfo;
        } catch (Exception e) {
            LogUtil.e("DataBaseHelper-queryByNumberSql:" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return userInfo;
    }

}
