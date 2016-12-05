package com.dajiangtai.djt_spider.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 页面下载工具类
 * @author dajiangtai
 * created by 2016-10-28
 *
 */
public class PageDownLoadUtil {
	private final static String USER_AGENT="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	
	public static String getPageContent(String url){
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = null;
		/******************设置动态ip***********************/
		//String proxy_ip = "182.90.28.52";
		//int proxy_port = 80;
		RedisUtil redisUtil = new RedisUtil();
		//获取代理ip
		String ip_port = redisUtil.getSet("proxy");
		if(StringUtils.isNotBlank(ip_port)){
			String[] arr = ip_port.split(":");
			String proxy_ip = arr[0];
			int proxy_port = Integer.parseInt(arr[1]);
			//设置代理
			HttpHost proxy = new HttpHost(proxy_ip, proxy_port);
			client = builder.setProxy(proxy).build();
		}else{
			client = builder.build();
		}		
		/*******************设置动态ip*********************/
		//CloseableHttpClient client = builder.build();
		
		HttpGet request = new HttpGet(url);
		String content = null;
		try {
			request.setHeader("User-Agent",USER_AGENT);
			CloseableHttpResponse response = client.execute(request);
			HttpEntity  entity = response.getEntity();
			content = EntityUtils.toString(entity);
		} catch(HttpHostConnectException e){
			e.printStackTrace();
			//如果当前ip不可用，从动态代理ip库里面删除
			redisUtil.deleteSet("proxy", ip_port);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RedisUtil redisUtil = new RedisUtil();		
		redisUtil.addSet("proxy", "60.168.245.128:808");
		redisUtil.addSet("proxy", "110.73.4.87:8123");
		redisUtil.addSet("proxy", "59.37.199.227:9797");
	}

}
