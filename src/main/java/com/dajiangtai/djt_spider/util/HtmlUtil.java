package com.dajiangtai.djt_spider.util;

import java.util.regex.Pattern;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
/**
 * 页面解析工具类
 * @author dajiangtai
 * created by 2016-10-29
 */
public class HtmlUtil {
	
	/**
	 * 获取标签属性值
	 * @param tagNode
	 * @param xpath
	 * @param att
	 * @return
	 */
	public static String getAttributeByName(TagNode tagNode,String xpath,String att){
		String result = null;
		Object[] evaluateXPath = null;
		try {
			evaluateXPath = tagNode.evaluateXPath(xpath);
			if(evaluateXPath.length>0){
				TagNode node = (TagNode) evaluateXPath[0];
				result = node.getAttributeByName(att);
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}

	/**
	 * 通过xpath，regex获取字段
	 * @param rootNode
	 * @param xpath
	 * @param regex
	 * @return
	 */
	public static String getFieldByRegex(TagNode rootNode,String xpath,String regex){
		String number = "0";
		Object[] evaluateXPath = null;
		try {
			evaluateXPath=rootNode.evaluateXPath(xpath);
			 if(evaluateXPath.length>0){
				 TagNode node = (TagNode) evaluateXPath[0];
				 
				 Pattern numberPattern = Pattern.compile(regex, Pattern.DOTALL);
				 number = RegexUtil.getPageInfoByRegex(node.getText().toString(), numberPattern, 0);
			 }
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return number;		
	}
}
