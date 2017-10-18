package com.ctid.intelsmsapp.net;

/**
 * 同步短信平台同步数据到客服端数据库接口
 *
 * @author 蒋艳军
 *         2017-10-16
 */
public interface ISynDataService {

    /**
     * 同步平台数据到本地数据库
     *
     * @param dataSynType 数据同步类型，1 自动，2 手动， 3 内置数据
     * @throws Exception
     */
    boolean autoSysPlatDataToLocalDB(int dataSynType) throws Exception;

}
