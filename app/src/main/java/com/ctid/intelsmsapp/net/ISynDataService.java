package com.ctid.intelsmsapp.net;

import com.ctid.intelsmsapp.bean.resbean.ReturnDataVo;

import java.util.List;

/**
 * 同步短信平台同步数据到客服端数据库接口
 * @author 蒋艳军
 * 2017-10-16
 */
public interface ISynDataService {

    /**
     * 同步平台数据到本地数据库
     * @throws Exception
     */
    public boolean autoSysPlatDataToLocalDB() throws Exception;


    /**
     * 向短信数据平台发起请求，获取全量数据
     * @return
     * @throws Exception
     */
    public List<ReturnDataVo>  downLoadAllDataFromIntelSmsPlat() throws Exception;

    /**
     * 同步平台数据到本地数据库
     * @param dataList 平台数据
     * @throws Exception
     */
    public void synDataToLocalDB(List<ReturnDataVo> dataList) throws  Exception;

}
