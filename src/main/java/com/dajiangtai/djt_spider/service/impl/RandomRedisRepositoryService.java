package com.dajiangtai.djt_spider.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IRepositoryService;
import com.dajiangtai.djt_spider.util.MatchUtil;
import com.dajiangtai.djt_spider.util.RedisUtil;
/**
 * Redis url仓库实现类:随机取不同视频网站url，降低单个网站频繁访问
 * @author dajiangtai
 *
 */
public class RandomRedisRepositoryService implements IRepositoryService {
	//顶级域名+redisKey
	HashMap<String, String> hashMap = new HashMap<String, String>();
	Random random = new Random();
	RedisUtil redisUtil = new RedisUtil();

	public String poll() {
		// TODO Auto-generated method stub
		String[] keyArr = hashMap.keySet().toArray(new String[0]);
		int nextInt = random.nextInt(keyArr.length);
		String key = keyArr[nextInt];
		String value = hashMap.get(key);
		return redisUtil.poll(value);
	}

	public void addHighLevel(String url) {
		// TODO Auto-generated method stub
		//获取顶级域名
		String topDomain = MatchUtil.getTopDomain(url);
		//根据顶级域名获取redis key
		String redisKey = hashMap.get(topDomain);
		if(redisKey==null){
			redisKey = topDomain;
			hashMap.put(topDomain, redisKey);
		}
		redisUtil.add(topDomain, url);
	}

	public void addLowLevel(String url) {
		// TODO Auto-generated method stub
		addHighLevel(url);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RedisUtil redisUtil = new RedisUtil();		
//		redisUtil.addSet("djt", "dajiangtai");
//		redisUtil.addSet("djt", "dajiangtai2");
//		redisUtil.addSet("djt", "dajiangtai3");
//		redisUtil.addSet("djt", "dajiangtai4");
//		redisUtil.addSet("djt", "dajiangtai5");
		
		redisUtil.deleteSet("djt", "dajiangtai3");
	}

}
