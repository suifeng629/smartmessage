package com.ctid.intelsmsapp.net.impl;

import com.ctid.intelsmsapp.net.HttpControllerService;
import com.ctid.intelsmsapp.utils.LogUtil;
import com.ctid.intelsmsapp.utils.http.CallHttpClient;

/**
 * http调用接口
 * @author 蒋艳军
 *
 */
public class HttpControllerServiceImpl implements HttpControllerService {

    
    @Override
    public String executeHttpsPost(String url, String params) throws Exception {
    	String retSrc = "";
			try {
			 retSrc = CallHttpClient.callPost(url,params, 10000);
			 System.out.println(retSrc);
				LogUtil.d("executeHttpsPost-http请求结果信息："+retSrc);
			}catch (Exception e) {
				e.printStackTrace();
//				LogUtil.d("http请求异常信息："+e.getMessage());
//				LogUtil.d("http请求异常堆栈信息："+e.getStackTrace());
			} finally {
//				LogUtil.d("http请求执行完毕");
		}
    	return retSrc;
    }
    
    @Override
    public String executeHttpsGet(String url, String params) throws Exception {
    	String retSrc = executeHttpsPost(url,params);
    	return retSrc;
    }
    
}
