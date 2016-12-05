package com.dajiangtai.djt_spider.util;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 操作redis数据库的工具类
 *  Created by dajiangtai
 *
 */
public class RedisUtil {
	
	//redis中列表key的名称
	public static String highkey = "spider.highlevel";
	public static String lowkey = "spider.lowlevel";
	
	public static String starturl="start.url";
	JedisPool jedisPool = null;
	public RedisUtil(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		jedisPool = new JedisPool(poolConfig, "192.168.20.129", 6379);
	}
	
	/**
	 * 查询
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key,int start,int end){
		Jedis resource = jedisPool.getResource();
		
		List<String> list = resource.lrange(key, start, end);
		jedisPool.returnResourceObject(resource);
		return list;
		
	}
	
	/**
	 * 添加list
	 * @param Key
	 * @param url
	 */
	public void add(String Key, String url) {
		Jedis resource = jedisPool.getResource();
		resource.lpush(Key, url);
		jedisPool.returnResourceObject(resource);
	}
	
	
	/**
	 * 获取
	 * @param key
	 * @return
	 */
	public String poll(String key) {
		Jedis resource = jedisPool.getResource();
		String result = resource.rpop(key);
		jedisPool.returnResourceObject(resource);
		return result;
	}
	
	/**
	 * 添加set
	 * @param Key
	 * @param url
	 */
	public void addSet(String Key, String value) {
		Jedis resource = jedisPool.getResource();
		resource.sadd(Key, value);
		jedisPool.returnResourceObject(resource);
	}
	
	/**
	 * 随机获取Set 值
	 * @param Key
	 * @param url
	 */
	public String getSet(String key) {
		Jedis resource = jedisPool.getResource();
		String value = resource.srandmember(key);
		jedisPool.returnResourceObject(resource);
		return value;
	}
	
	/**
	 * 删除Set 随机值
	 * @param Key
	 * @param value
	 */
	public void deleteSet(String key,String value){
		Jedis resource = jedisPool.getResource();
		resource.srem(key, value);	
		jedisPool.returnResourceObject(resource);
	}
	
	public static void main(String[] args) {
		RedisUtil redisUtil = new RedisUtil();
		String url = "http://tv.youku.com/search/index/_page40177_1_cmodid_40177";
		redisUtil.add(highkey, url);
		}
	}