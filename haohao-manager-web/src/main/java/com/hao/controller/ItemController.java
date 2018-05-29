package com.hao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.pojo.TbItem;
import com.hao.pojo.TbItemParam;
import com.hao.service.ItemService;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 显示商品列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIResult getItemList(Integer page, Integer rows){
		EasyUIResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 显示商品类目
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getEasyUITreeNodeList(@RequestParam(value="id", defaultValue="0") Long parentId){
		List<EasyUITreeNode> nodeList = itemService.getEasyUITreeNodeList(parentId);
		return nodeList;
	}
	
	/**
	 * 添加商品
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public HaohaoResult saveItem(TbItem item, String desc, String itemParams){
		try {
			HaohaoResult result = itemService.saveItem(item, desc, itemParams);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "服务器错误");
		}
	}
	
	/**
	 * 显示规格列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/param/list")
	@ResponseBody
	public EasyUIResult getParamList(Integer page, Integer rows){
		EasyUIResult result = itemService.getParamList(page, rows);
		return result;
	}
	
	@RequestMapping("/param/query/itemcatid/{itemcatid}")
	@ResponseBody
	public HaohaoResult selectItemCatById(@PathVariable Long itemcatid){
		HaohaoResult result =itemService.selectParamItemCatExistsById(itemcatid);
		return result;
	}
	
	@RequestMapping("/param/save/{itemcatid}")
	@ResponseBody
	public HaohaoResult saveItemParam(@PathVariable Long itemcatid, TbItemParam itemParam){
		try {
			HaohaoResult result = itemService.saveItemParam(itemcatid, itemParam);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥不稳！");
		}
	}
	
	@RequestMapping(value="/param/delete", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult deleteItemParamById(String ids){
		try {
			HaohaoResult result = itemService.deleteItemParamById(ids);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥不稳！");
		}
		
	}
	
	@RequestMapping("/param/item/query/{itemId}")
	@ResponseBody
	public HaohaoResult getItemParamItemById(@PathVariable Long itemId){
		try {
			HaohaoResult result = itemService.getItemParamItemById(itemId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥很慌!");
		}
	}
	
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public HaohaoResult getItemDescById(@PathVariable Long itemId){
		try {
			HaohaoResult result = itemService.getItemDescById(itemId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥不妙了！");
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public HaohaoResult updateItemAll(TbItem item, String desc, String itemParams, Long itemParamId){
		try {
			HaohaoResult result = itemService.updateItemAll(item, desc, itemParams, itemParamId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥僵住了！");
		}
	}
	
	@RequestMapping(value="/reshelf", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult updateItemReshelf(String ids){
		//上架
		HaohaoResult result = itemService.updateItemReshelf(ids);
		return result;
	}
	
	@RequestMapping(value="/instock", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult updateItemInstock(String ids){
		//下架
		HaohaoResult result = itemService.updateItemInstock(ids);
		return result;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult deleteItemByIds(String ids){
		//删除
		HaohaoResult result = itemService.deleteItemByIds(ids);
		return result;
	}
	
	
	
	
	
	
	
	
	
}
