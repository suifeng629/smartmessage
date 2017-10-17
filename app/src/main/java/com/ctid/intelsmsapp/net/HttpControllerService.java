package com.ctid.intelsmsapp.net;

/**
 * http调用接口
 * @author 蒋艳军
 *
 */
public interface HttpControllerService {
	
	/**
	 * post 方式访问
	 * @param host
	 * @param params
	 * @return
	 * @throws Exception
	 */
    public String executeHttpsPost(String host, String params) throws Exception;
    
    /**
     * get 方式访问
     * @param host
     * @param params
     * @return
     * @throws Exception
     */
    public String executeHttpsGet(String host, String params) throws Exception;
    
}
