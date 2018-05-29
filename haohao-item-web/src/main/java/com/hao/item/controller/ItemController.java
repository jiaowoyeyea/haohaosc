package com.hao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hao.item.pojo.Item;
import com.hao.pojo.TbItem;
import com.hao.pojo.TbItemDesc;
import com.hao.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId ,Model model){
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc itemDesc = (TbItemDesc) itemService.getItemDescById(itemId).getData();
		
		Item item = new Item(tbItem);
		
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
		
	}
	
	
}
