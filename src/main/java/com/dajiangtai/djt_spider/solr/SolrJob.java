package com.dajiangtai.djt_spider.solr;
import org.apache.commons.lang.StringUtils;
import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.util.HbaseUtil;
import com.dajiangtai.djt_spider.util.RedisUtil;
import com.dajiangtai.djt_spider.util.SolrUtil;
import com.dajiangtai.djt_spider.util.ThreadUtil;

/**
 * 建立索引
 * Created by dajiangtai 
 *
 */
public class SolrJob{
	private static final String SOLR_TV_INDEX = "solr_tv_index";
	static RedisUtil redis = new RedisUtil();
	
	
	public static void buildIndex(){
		String tvId = "";
		try {
			System.out.println("开始简历索引！！！");
			HbaseUtil hbaseUtil = new HbaseUtil();
			tvId = redis.poll(SOLR_TV_INDEX);
			while (!Thread.currentThread().isInterrupted()) {
				if(StringUtils.isNotBlank(tvId)){
					Page page = hbaseUtil.get(HbaseUtil.TABLE_NAME, tvId);
					if(page !=null){
						SolrUtil.addIndex(page);
					}
					tvId = redis.poll(SOLR_TV_INDEX);
				}else{
					System.out.println("目前没有需要索引的数据，休息一会再处理！");
					ThreadUtil.sleep(5000);
				}
			}
		} catch (Exception e) {
			redis.add(SOLR_TV_INDEX, tvId);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		buildIndex();
	}
}
