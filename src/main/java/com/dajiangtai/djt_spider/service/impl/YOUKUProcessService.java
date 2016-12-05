package com.dajiangtai.djt_spider.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IProcessService;
import com.dajiangtai.djt_spider.util.HtmlUtil;
import com.dajiangtai.djt_spider.util.LoadPropertyUtil;
import com.dajiangtai.djt_spider.util.RegexUtil;
/**
 * 优酷页面解析实现类
 * @author dajiangtai
 * created by 2016-10-28
 */
public class YOUKUProcessService implements IProcessService {
	public void process(Page page) {
		// TODO Auto-generated method stub
		
		//System.out.println("正在解析优酷页面信息！！！！！！！！！！！！！！！");
		String content = page.getContent();
		
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(content);
		
		if(page.getUrl().startsWith("http://www.youku.com/show_page")){
			//解析电视剧详情页
			parseDetail(page,rootNode);
			//System.out.println("解析电视剧详情页！！！！！！！！！！！！！！！！！！！！！");
		}else{
			//System.out.println("解析电视剧列表页！！！！！！！！！！！！！！！！！！！！！");
			String nextUrl = HtmlUtil.getAttributeByName(rootNode, LoadPropertyUtil.getYOUKU("nextUrlRegex"), "href");
			if(nextUrl != null){
				page.addUrl(nextUrl);
			}
			//System.out.println("urlList="+nextUrl);
			//获取详情页的url
			try {
				Object[] evaluateXPath = rootNode.evaluateXPath(LoadPropertyUtil.getYOUKU("eachDetailUrlRegex"));
				if(evaluateXPath.length>0){
					for(Object object :evaluateXPath){
						TagNode tagNode = (TagNode) object;
						
						//提取每日播放增量
						String daynumber = HtmlUtil.getFieldByRegex(tagNode, LoadPropertyUtil.getYOUKU("eachDetailUrlRegex_span"), LoadPropertyUtil.getYOUKU("commonRegex"));
						
						
						//提取电视剧详情url
						String url = HtmlUtil.getAttributeByName(tagNode, LoadPropertyUtil.getYOUKU("eachDetailUrlRegex_a"), "href");
						//String detailUrl = tagNode.getAttributeByName("href");
						//page.addUrl(detailUrl);
						//System.out.println("detailUrl="+detailUrl);
						if(url != null){
							//电视剧详情页url携带播放增量信息
							page.addUrl((url+"@"+daynumber));
						}						
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
		 ////*[@id="showInfo"]/div/div[1]/ul[2]/li[1]/span[1]
		 //获取总播放量
		 String allnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseAllNumber"), LoadPropertyUtil.getYOUKU("allnumberRegex"));
		 page.setAllnumber(allnumber);
		 //System.out.println(" 总播放数:"+allnumber);


		 
		 //获取评论数
		 String commentnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseCommentNumber"), LoadPropertyUtil.getYOUKU("commentnumberRegex"));
		 page.setCommentnumber(commentnumber);
		 //System.out.println(" 评论数:"+commentnumber);
		 
		 //获取赞
		 String supportnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseSupportNumber"), LoadPropertyUtil.getYOUKU("supportnumberRegex"));
		 page.setSupportnumber(supportnumber);
		 //System.out.println(" 赞:"+supportnumber);
		 
		 //page.setDaynumber("0");
		 page.setAgainstnumber("0");
		 page.setCollectnumber("0");
		 
		 //获取优酷电视剧id
		 Pattern pattern = Pattern.compile(LoadPropertyUtil.getYOUKU("idRegex"), Pattern.DOTALL);
		 page.setTvId("youku_"+RegexUtil.getPageInfoByRegex(page.getUrl(), pattern, 1));
		 
		 //获取优酷电视TvName
		 page.setTvname(HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseTvName"), LoadPropertyUtil.getYOUKU("commonRegex")));
	}

}
