package com.hao.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import hao.common.utils.SearchResult;

public interface SearchDao {
	
	/**
	 * 根据查询条件执行查询封装结果集
	 * @param query
	 * @return
	 */
	SearchResult search(SolrQuery query) throws Exception ;

}
