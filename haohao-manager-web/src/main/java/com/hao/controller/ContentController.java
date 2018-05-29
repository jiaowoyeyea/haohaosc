package com.hao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.content.service.ContentService;
import com.hao.pojo.TbContent;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;

@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryNode(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> list = contentService.getContentCategoryNode(parentId);
		return list;
	}
	
	@RequestMapping(value="/query/list", method=RequestMethod.GET)
	@ResponseBody
	public EasyUIResult getContentCategoryTreeList(Long categoryId, Integer page, Integer rows){
		EasyUIResult result = contentService.getContentCategoryTreeList(categoryId, page, rows);
		return result;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public HaohaoResult saveContent(TbContent content){
		HaohaoResult result = contentService.saveContent(content);
		return result;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public HaohaoResult updateContent(TbContent content){
		HaohaoResult result = contentService.updateContent(content);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public HaohaoResult deleteContent(String ids){
		HaohaoResult result = contentService.deleteContent(ids);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
