package com.ctid.intelsmsapp.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * File Utility
 * Created by jiangyanjun on 2017/10/17.
 */

public class FileUtil {

    /**
     * Read raw file and return str
     *
     * @param context     Application context
     * @param rawFileName Name of the raw file
     * @return String of raw file
     */
    public static String readFile(Context context, String rawFileName) throws Exception {
        int resId = context.getResources().getIdentifier(rawFileName, "raw", context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(resId);
        return readStrFromStream(inputStream);
    }


    private static String readStrFromStream(InputStream inputStream) throws Exception {

        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        bufferedReader.close();
        inputStream.close();
        return stringBuilder.toString();
    }


}
