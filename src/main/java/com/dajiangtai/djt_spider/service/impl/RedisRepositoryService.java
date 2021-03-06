package com.dajiangtai.djt_spider.service.impl;

import org.apache.commons.lang.StringUtils;

import com.dajiangtai.djt_spider.service.IRepositoryService;
import com.dajiangtai.djt_spider.util.RedisUtil;
/**
 * Redis url仓库实现类
 * @author dajiangtai
 *
 */
public class RedisRepositoryService implements IRepositoryService {
	RedisUtil redisUtil = new RedisUtil();
	public String poll() {
		// TODO Auto-generated method stub
		String url = redisUtil.poll(RedisUtil.highkey);
		if(StringUtils.isBlank(url)){
			url = redisUtil.poll(RedisUtil.lowkey);
		}
		return url;
	}

	public void addHighLevel(String url) {
		// TODO Auto-generated method stub
		redisUtil.add(RedisUtil.highkey, url);
	}

	public void addLowLevel(String url) {
		// TODO Auto-generated method stub
		redisUtil.add(RedisUtil.lowkey, url);
	}

}
