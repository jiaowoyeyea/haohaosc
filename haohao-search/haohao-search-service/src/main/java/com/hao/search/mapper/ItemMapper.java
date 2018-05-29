package com.hao.search.mapper;

import java.util.List;

import hao.common.utils.SearchItem;

public interface ItemMapper {
	
	List<SearchItem> getItemList();

	SearchItem getItem(Long itemId);
}
