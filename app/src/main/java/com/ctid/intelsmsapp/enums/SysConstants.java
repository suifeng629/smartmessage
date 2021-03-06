package com.ctid.intelsmsapp.enums;

/**
 * 系统常量
 * Created by jiangyanjun on 2017/10/16.
 */

public class SysConstants {
    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";

    /**
     * 智能短信数据平台访问url
     * 本地： http://172.16.142.111:9080/w/service/apk/
     * 测试：http://test.wuyou189.com/ctid_policy/service/apk/
     */
    public static final String INTEL_SMS_SERVERS_URL = "http://192.168.155.1/w/service/apk/";

    /**
     * 数据同步类型：1：自动
     */
    public static final int DATA_SYN_TYPE_AUTO = 1;

    /**
     * 数据同步类型：2：手动
     */
    public static final int DATA_SYN_TYPE_HAND = 2;

    /**
     * 数据同步类型：3：内置
     */
    public static final int DATA_SYN_TYPE_IN = 3;

    /**
     * 模板类型：火车票
     */
    public static final String MODLE_TYPE_TRAIN = "train";
    /**
     * 模板类型：银行
     */
    public static final String MODLE_TYPE_BANK = "bank";
    /**
     * 模板类型：验证码
     */
    public static final String MODLE_TYPE_CODE = "code";

}
