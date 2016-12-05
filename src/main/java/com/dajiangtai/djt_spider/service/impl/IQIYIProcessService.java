package com.dajiangtai.djt_spider.service.impl;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IProcessService;
import com.dajiangtai.djt_spider.util.HtmlUtil;
import com.dajiangtai.djt_spider.util.LoadPropertyUtil;
/**
 * 爱奇艺页面解析实现类
 * @author dajiangtai
 * created by 2016-10-28
 */
public class IQIYIProcessService implements IProcessService {

	public void process(Page page) {
		// TODO Auto-generated method stub
			System.out.println("正在解析爱奇艺页面信息！！！！！！！！！！！！！！！");
			String content = page.getContent();
			
			HtmlCleaner htmlCleaner = new HtmlCleaner();
			TagNode rootNode = htmlCleaner.clean(content);
			if(page.getUrl().startsWith("http://www.iqiyi.com")){
				//System.out.println("解析爱奇艺电视剧详情页！！！！！！！！！！！！！！！！！！！！！");
				//解析电视剧详情页
				parseDetail(page,rootNode);				
			}else{
				//System.out.println("解析爱奇艺电视剧列表页！！！！！！！！！！！！！！！！！！！！！");
				String nextUrl = HtmlUtil.getAttributeByName(rootNode, LoadPropertyUtil.getIQIYI("nextUrlRegex"), "href");
				if(nextUrl != null){
					page.addUrl("http://list.iqiyi.com"+nextUrl);
				}
				//System.out.println("urlList="+nextUrl);
				//获取详情页的url
				try {
					Object[] evaluateXPath = rootNode.evaluateXPath(LoadPropertyUtil.getIQIYI("eachDetailUrlRegex"));
					if(evaluateXPath.length>0){
						for(Object object :evaluateXPath){
							TagNode tagNode = (TagNode) object;
							
							
							
							//提取电视剧详情url
							String url = HtmlUtil.getAttributeByName(tagNode, LoadPropertyUtil.getIQIYI("eachDetailUrlRegex_a"), "href");

							
							//电视剧详情页url携带播放增量信息
							page.addUrl((url));
						}
					}
				} catch (XPatherException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * 解析电视剧详情页
	 * @param page
	 * @param rootNode
	 */
	public void parseDetail(Page page,TagNode rootNode){
		
	}

}
