package com.dajiangtai.djt_spider.start;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IDownLoadService;
import com.dajiangtai.djt_spider.service.IProcessService;
import com.dajiangtai.djt_spider.service.IRepositoryService;
import com.dajiangtai.djt_spider.service.IStoreService;
import com.dajiangtai.djt_spider.service.impl.ConsoleStoreService;
import com.dajiangtai.djt_spider.service.impl.HBaseStoreService;
import com.dajiangtai.djt_spider.service.impl.HttpClientDownLoadService;
import com.dajiangtai.djt_spider.service.impl.IQIYIProcessService;
import com.dajiangtai.djt_spider.service.impl.QueueRepositoryService;
import com.dajiangtai.djt_spider.service.impl.RandomRedisRepositoryService;
import com.dajiangtai.djt_spider.service.impl.RedisRepositoryService;
import com.dajiangtai.djt_spider.service.impl.YOUKUProcessService;
import com.dajiangtai.djt_spider.util.LoadPropertyUtil;
import com.dajiangtai.djt_spider.util.ThreadUtil;

/**
 * 电视剧爬虫执行入口类
 * @author dajiangtai
 * created by 2016-10-28
 *
 */
public class StartDSJCount {
	private IDownLoadService downLoadSerivce ;
	private IProcessService processService;
	
	private IStoreService storeService;
	
	//private Queue<String> urlQueue = new ConcurrentLinkedDeque<String>();
	
	private IRepositoryService repositoryService;
	
	//固定线程池
	private ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Integer.parseInt(LoadPropertyUtil.getConfig("threadNum")));

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StartDSJCount dsj = new StartDSJCount();
		dsj.setDownLoadSerivce(new HttpClientDownLoadService());
		//dsj.setProcessService(new YOUKUProcessService());
		//dsj.setProcessService(new IQIYIProcessService());
		dsj.setStoreService(new ConsoleStoreService());
		//dsj.setStoreService(new HBaseStoreService());
		
		//dsj.setRepositoryService(new QueueRepositoryService());
		
		//dsj.setRepositoryService(new RedisRepositoryService());
		
		dsj.setRepositoryService(new RandomRedisRepositoryService());
		
		//String url = "http://www.youku.com/show_page/id_z9cd2277647d311e5b692.html?spm=a2htv.20005143.m13050845531.5~5~1~3~A&from=y1.3-tv-index-2640-5143.40177.1-1";
		String youkuUrl ="http://tv.youku.com/search/index/_page40177_1_cmodid_40177";
		String iqiyiUrl = "http://list.iqiyi.com/www/2/-------------10-3-1---.html";
		
		//设置起始url
		//dsj.urlQueue.add(url);
		
		dsj.repositoryService.addHighLevel(youkuUrl);
		dsj.repositoryService.addHighLevel(iqiyiUrl);
		//开启爬虫
		dsj.startSpider();
	}
	
	/**
	 * 开启一个爬虫入口
	 */
	public void startSpider(){
		while(true){
			//从队列中提取需要解析的url
			//String url = urlQueue.poll();
			
			//数据仓库提取解析url
			final String url = repositoryService.poll();
			
			//判断url是否为空
			if(StringUtils.isNotBlank(url)){
				newFixedThreadPool.execute(new Runnable(){

					public void run() {
						// TODO Auto-generated method stub
						//System.out.println("当前第"+Thread.currentThread().getId()+"个线程！");
						
						String[] detailUrl = url.split("@");
						
						//下载
						Page page = StartDSJCount.this.downloadPage(detailUrl[0]);
						
						if(detailUrl.length==2){
							page.setDaynumber(detailUrl[1]);
						}
						//下载
						//Page page = StartDSJCount.this.downloadPage(url);
						if(page.getUrl().contains("youku")){
							StartDSJCount.this.setProcessService(new YOUKUProcessService());
						}else {
							StartDSJCount.this.setProcessService(new IQIYIProcessService());
						}
						//解析
						StartDSJCount.this.processPage(page);
						List<String> urlList = page.getUrlList();
						for(String eachurl : urlList){
							//this.urlQueue.add(eachurl);
//							if(eachurl.startsWith("http://tv.youku.com/search/index")){
//								StartDSJCount.this.repositoryService.addHighLevel(eachurl);
//							}else{
//								StartDSJCount.this.repositoryService.addLowLevel(eachurl);
//							}
							StartDSJCount.this.repositoryService.addHighLevel(eachurl);
						}
						
						//不同网站存储判断
						if(page.getUrl().startsWith("http://www.youku.com/show_page")||page.getUrl().startsWith("http://www.iqiyi.com")){
							//存储数据
							StartDSJCount.this.storePageInfo(page);
						}
						
						ThreadUtil.sleep(Long.parseLong(LoadPropertyUtil.getConfig("millions_3")));
					}
					
				});								
			}else{
				System.out.println("队列中的电视剧url解析完毕，请等待！");
				ThreadUtil.sleep(Long.parseLong(LoadPropertyUtil.getConfig("millions_5")));
			}
		}
	}
	/**
	 * 下载页面
	 * @param url
	 * @return
	 */
	public Page downloadPage(String url){
		return this.downLoadSerivce.download(url);		
	}
	
	/**
	 * 页面解析
	 * @param page
	 */
	public void processPage(Page page){
		this.processService.process(page);
	}
	
	/**
	 * 存储页面信息
	 * @return
	 */
	public void storePageInfo(Page page){
		this.storeService.store(page);
	}
	public IDownLoadService getDownLoadSerivce() {
		return downLoadSerivce;
	}

	public void setDownLoadSerivce(IDownLoadService downLoadSerivce) {
		this.downLoadSerivce = downLoadSerivce;
	}

	public IProcessService getProcessService() {
		return processService;
	}

	public void setProcessService(IProcessService processService) {
		this.processService = processService;
	}

	public IStoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(IStoreService storeService) {
		this.storeService = storeService;
	}

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	
}
