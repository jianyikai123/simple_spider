package com.dajiangtai.djt_spider.service;
/**
 * 存储url仓库接口
 * @author dajiangtai
 *
 */
public interface IRepositoryService {

	public String poll();
	
	public void addHighLevel(String url);
	
	public void addLowLevel(String url);
}
