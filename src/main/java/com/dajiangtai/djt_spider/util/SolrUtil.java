package com.dajiangtai.djt_spider.util;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dajiangtai.djt_spider.entity.Page;

/**
 * solr工具类 Created by dajiangtai 
 */
public class SolrUtil {
	// solr服务器地址
	private static final String SOLR_URL = "http://localhost:8983/solr"; 
	private static HttpSolrServer server = null;
	static {
		try {
			server = new HttpSolrServer(SOLR_URL);
			server.setAllowCompression(true);
			server.setConnectionTimeout(10000);
			server.setDefaultMaxConnectionsPerHost(100);
			server.setMaxTotalConnections(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立索引
	 * 
	 * @throws Exception
	 */
	public static void addIndex(Page page) throws Exception {
		server.addBean(page);
		// 对索引进行优化
		server.optimize();
		server.commit();
		System.out.println("添加电视剧索引:" + page.getTvId());
	}
	
	/**
	 * 
	 * 删除索引
	 * @throws Exception
	 */
	public static void delIndex() {
		try {
			server.deleteByQuery("*:*");
			// 要记得提交数据
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列表查询
	 * 
	 * @param skey
	 * @param range
	 * @param start
	 * @param sort
	 * @throws Exception
	 */
	public static List<Page> search(String skey, int start, int range,
			String sort,String field) throws Exception {
		SolrQuery params = new SolrQuery();
		if (StringUtils.isNotBlank(skey)) {
			params.set("q","tvname:"+skey);
		} else {
			params.set("q", "*:*");
		}
		params.set("start", "" + start);
		params.set("rows", "" + range);
		
		if(StringUtils.isNotBlank(sort)){
			if(sort.equals("asc")){
				params.setSort(field, SolrQuery.ORDER.asc);
			}else{
				params.setSort(field, SolrQuery.ORDER.desc);
			}
		}
		QueryResponse response = server.query(params);

		List<Page> results = response.getBeans(Page.class);
		return results;
	}

	/**
	 * 列表查询
	 * 
	 * @param skey
	 * @param range
	 * @param start
	 * @param sort
	 * @throws Exception
	 */
	public static Page searchPage(String skey) throws Exception {
		SolrQuery params = new SolrQuery();
		params.set("tvId", skey);
		QueryResponse response = server.query(params);
		List<Page> pages = response.getBeans(Page.class);
		return pages.get(0);
	}
	
	/**
	 * 根据条件查询总记录数
	 * @param skey
	 * @return
	 */
	public static int getCount(String skey) {
		int count = 0;
		SolrQuery params = new SolrQuery();
		if (StringUtils.isNotBlank(skey)) {
			params.set("q", "tvname:"+skey);
		} else {
			params.set("q", "*:*");
		}
		try {
			QueryResponse response = server.query(params);
			count = (int) response.getResults().getNumFound();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return count;
	}

	public static void main(String[] args) {
		//delIndex();
		List<Page> pageList = new ArrayList<Page>();
		int count = 0;
		try {
			pageList = SolrUtil.search("传奇", 0, 10, "","");
			for(Page page : pageList){
				System.out.println("tvname:"+page.getTvname());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(JSONArray.fromObject(pageList).toString());
	}
}
