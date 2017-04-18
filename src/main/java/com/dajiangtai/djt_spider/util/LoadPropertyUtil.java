package com.dajiangtai.djt_spider.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 读取配置文件属性工具类
 * 
 * @author dajiangtai created by 2016-10-29
 */
public class LoadPropertyUtil {

	// 读取爱奇艺配置文件
	public static String getIQIYI(String key) {
		String value = "";
		Locale locale = Locale.getDefault();
		try {
			ResourceBundle localResource = ResourceBundle.getBundle("iqiyi",
					locale);
			value = localResource.getString(key);
		} catch (MissingResourceException mre) {
			value = "";
		}
		return value;
	}

	// 读取优酷配置文件
	public static String getYOUKU(String key) {
		String value = "";
		Locale locale = Locale.getDefault();
		try {
			ResourceBundle localResource = ResourceBundle.getBundle("youku",
					locale);
			value = localResource.getString(key);
		} catch (MissingResourceException mre) {
			value = "";
		}
		return value;
	}

	// 读取公共配置文件
	public static String getConfig(String key) {
		String value = "";
		Locale locale = Locale.getDefault();
		try {
			ResourceBundle localResource = ResourceBundle.getBundle("config",
					locale);
			value = localResource.getString(key);
		} catch (MissingResourceException mre) {
			value = "";
		}
		return value;
	}

	public static void main(String[] args) {
		System.out.println(getConfig("threadNum"));
		System.out.println(getYOUKU("commonRegex"));
	}
}
