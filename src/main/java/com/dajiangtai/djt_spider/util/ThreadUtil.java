package com.dajiangtai.djt_spider.util;
/**
 * 线程工具类
 * @author dajiangtai
 *
 */
public class ThreadUtil {

	@SuppressWarnings("static-access")
	public static void sleep(long millions){
		try {
			Thread.currentThread().sleep(millions);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
