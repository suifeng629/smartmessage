package com.ctid.intelsmsapp.utils;

import android.text.TextUtils;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/20
 * @email sunzhiwei@ctid.com.cn
 */
public class FuncString {
    /**
     * 查询商户信息时，手机号码带有+86信息，去除处理
     */
    public static String getPhoneNumber(String number){
        if(!TextUtils.isEmpty(number)){
            if(number.startsWith("+86")){
                return number.substring(3);
            }else {
                return number;
            }
        }else {
            return null;
        }
    }
}
