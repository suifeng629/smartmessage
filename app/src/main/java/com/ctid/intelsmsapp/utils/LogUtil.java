package com.ctid.intelsmsapp.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtil {

    private static final String LOG_TAG = "testDemos";

    public static void d(String message) {
        Log.d(LOG_TAG, message);
    }

    public static void i(String message) {
        Log.i(LOG_TAG, message);
    }

    public static void w(String message) {
        Log.w(LOG_TAG, message);
    }

    public static void e(String message, Throwable tr) {
        Log.e(LOG_TAG, message, tr);
    }

    public static void e(String message) {
        Log.e(LOG_TAG, message);
    }

}
