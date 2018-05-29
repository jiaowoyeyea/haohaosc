/**
 * 
 */
package com.hao.search.service;

import hao.common.utils.HaohaoResult;

/**
 * @author 75659
 *
 */
public interface IndexService {
	
	/**
	 * 导入商品数据生成索引
	 * @return
	 */
	HaohaoResult indexImport() throws Exception ;
	
	/**
	 * 商品添加索引同步
	 * @param itemId
	 * @throws Exception 
	 */
	void addDocument(Long itemId) throws Exception;

}
