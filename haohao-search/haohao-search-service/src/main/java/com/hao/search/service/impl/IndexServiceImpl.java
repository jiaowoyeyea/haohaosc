package com.hao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hao.search.mapper.ItemMapper;
import com.hao.search.service.IndexService;

import hao.common.utils.HaohaoResult;
import hao.common.utils.SearchItem;

@Service
public class IndexServiceImpl implements IndexService {
	
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public HaohaoResult indexImport() throws Exception {
		// 
		List<SearchItem> itemList = itemMapper.getItemList();
		for (SearchItem searchItem : itemList) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", searchItem.getId());
			doc.addField("item_title", searchItem.getTitle());
			doc.addField("item_sell_point", searchItem.getSell_point());
			doc.addField("item_price", searchItem.getPrice());
			doc.addField("item_image", searchItem.getImage());
			doc.addField("item_category_name", searchItem.getCategory_name());
			doc.addField("item_desc", searchItem.getItem_desc());
			//写入索引库
			solrServer.add(doc);
		}
		solrServer.commit();
		return HaohaoResult.ok();
	}

	@Override
	public void addDocument(Long itemId) throws Exception {
		// 
		SearchItem searchItem = itemMapper.getItem(itemId);
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", searchItem.getId());
		doc.addField("item_title", searchItem.getTitle());
		doc.addField("item_sell_point", searchItem.getSell_point());
		doc.addField("item_price", searchItem.getPrice());
		doc.addField("item_image", searchItem.getImage());
		doc.addField("item_category_name", searchItem.getCategory_name());
		doc.addField("item_desc", searchItem.getItem_desc());
		//写入索引库
		solrServer.add(doc);
	solrServer.commit();
	
	}

}
