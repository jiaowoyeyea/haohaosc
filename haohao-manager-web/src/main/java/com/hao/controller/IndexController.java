package com.hao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.search.service.IndexService;

import hao.common.utils.HaohaoResult;

@Controller
public class IndexController {
	
	@Autowired
	private IndexService indexService;
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		
		return page;
	}
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public HaohaoResult indexImport(){
		try {
			HaohaoResult result = indexService.indexImport();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, "老哥不稳！");
		}
	}
}
