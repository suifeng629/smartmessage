package com.ctid.intelsmsapp.utils.http;

import com.ctid.intelsmsapp.utils.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CallHttpClient {

	/**
	 * 调用其他服务的api
	 * 
	 * @param url
	 *            服务的地址
	 * @param paramStr
	 *            请求参数 key=value&key1=value1
	 * @return 服务返回的json
	 */
	public static String callGet(String url, String paramStr, int timeOut) {
		HttpClient httpClient = new HttpClient();
		httpClient.setDefaultContentEncoding("utf-8");
		url = url + "?" + paramStr;
		String ret = "";

		ServerInfo serverInfo;
		try {
			serverInfo = httpClient.sendPost(url, timeOut);
			ret = serverInfo.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String callPost(String url, String paramStr, int timeOut) {
		HttpClient httpClient = new HttpClient();
		httpClient.setDefaultContentEncoding("utf-8");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (paramStr != null && !"".equals(paramStr)) {
			String[] params = paramStr.split("&");
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				param=param.replace("+", "%2b");
				if (param != null && !"".equals(param)) {
					String[] key_values = param.split("=");
					if (key_values.length == 2) {
						paramMap.put(key_values[0], key_values[1]);
						System.out.println("参数值=-==》"+key_values[1].toString());
					}
				}
			}
		}
		String ret = "";

		ServerInfo serverInfo;
		try {
			serverInfo = httpClient.sendPost(url, paramMap, timeOut);
			ret = serverInfo.getContent();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.d("CallHttpClient-callPost-http请求异常信息："+e.getMessage());
		}
		return ret;
	}
}
