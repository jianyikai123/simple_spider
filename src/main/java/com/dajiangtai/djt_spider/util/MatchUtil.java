package com.dajiangtai.djt_spider.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 匹配工具类
 * @author dajiangtai
 *
 */
public class MatchUtil {
	public static String getTopDomain(String url){
		String topDomain = null ;
		try {
			String host = new URL(url).getHost().toLowerCase();
			Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk)");
			Matcher matcher = pattern.matcher(host);
			while (matcher.find()) {
				topDomain = matcher.group();
				return topDomain;
			}
		} catch (MalformedURLException e) {
			System.out.println("url="+url);
			e.printStackTrace();
		}
		return topDomain;
	}
	
	public static void main(String[] args) {
		String host = MatchUtil.getTopDomain("http://shiju.tax861.gov.cn/nsfw/sscx/sgs.asp#");
		System.out.println(host);
	}
}
