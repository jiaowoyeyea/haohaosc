package com.hao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hao.search.dao.SearchDao;

import hao.common.utils.SearchItem;
import hao.common.utils.SearchResult;
@Repository
public class SearchDaoImpl implements SearchDao {
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public SearchResult search(SolrQuery query) throws Exception {
		// 
		QueryResponse response = solrServer.query(query);
		
		SolrDocumentList solrDocumentList = response.getResults();
		
		long numFound = solrDocumentList.getNumFound();
		
		SearchResult result = new SearchResult();
		
		result.setRecordCount(numFound);
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		List<SearchItem> itemList = new ArrayList<>();
		
		for (SolrDocument solrDocument : solrDocumentList) {
			
			SearchItem item = new SearchItem();
			
			item.setId((String) solrDocument.get("id"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			//取高亮显示
			
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			
			String title = "";
			
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			//添加到商品列表
			itemList.add(item);
		}
		result.setItemList(itemList);
		return result;
	}

}
