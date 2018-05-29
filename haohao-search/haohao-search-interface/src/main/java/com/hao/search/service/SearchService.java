package com.hao.search.service;

import hao.common.utils.SearchResult;

public interface SearchService {
	
	/**
	 * 商品搜索
	 * @param keyword
	 * @param page
	 * @param pAGE_ROWS
	 * @return
	 */
	SearchResult search(String keyword, Integer page, Integer pAGE_ROWS) throws Exception ;

}
