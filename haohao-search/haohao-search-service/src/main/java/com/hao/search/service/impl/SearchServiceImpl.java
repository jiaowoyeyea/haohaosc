package com.hao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hao.search.dao.SearchDao;
import com.hao.search.service.SearchService;

import hao.common.utils.SearchResult;

@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearchDao searchDao;
	
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	
	@Override
	public SearchResult search(String keyword, Integer page, Integer pAGE_ROWS) throws Exception {
		// 设置查询条件
		SolrQuery query = new SolrQuery();
		query.setQuery(keyword);
		//设置分页条件
		query.setStart((page-1)*pAGE_ROWS);
		query.setRows(pAGE_ROWS);
		//设置默认搜索域
		query.set("df", DEFAULT_FIELD);
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField(DEFAULT_FIELD);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//执行查询
		SearchResult searchResult = searchDao.search(query);
		searchResult.setTotalPages((int) ((searchResult.getRecordCount() + pAGE_ROWS - 1)/pAGE_ROWS));
		return searchResult;
	}

}
