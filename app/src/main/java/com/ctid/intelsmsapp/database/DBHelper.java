package com.ctid.intelsmsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ctid.intelsmsapp.utils.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database Helper Class
 * Created by Logan on 2017/10/14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.w("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(db);
    }

    /**
     * Create table
     *
     * @param sqlFileName 用来创建表的SQL文件名
     * @param db          SQLiteDatabase instant
     * @throws Exception
     */
    private void createTable(String sqlFileName, SQLiteDatabase db) throws Exception {
        int resId = context.getResources().getIdentifier(sqlFileName, "raw", context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        LogUtil.e("Exec SQL : " + stringBuilder.toString());
        db.execSQL(stringBuilder.toString()); //exec SQL
        inputStream.close();
    }

    /**
     * Insert init data
     *
     * @param sqlFileName 包含有insert语句的SQL文件名
     * @param db          SQLiteDatabase instant
     * @throws Exception
     */
    private void insertData(String sqlFileName, SQLiteDatabase db) throws Exception {
        int resId = context.getResources().getIdentifier(sqlFileName, "raw", context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            LogUtil.e("Exec SQL : " + str);
            db.execSQL(str); //exec SQL line by line
        }
        inputStream.close();
    }
}
