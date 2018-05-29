package com.hao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hao.search.service.SearchService;

import hao.common.utils.SearchResult;

/**
 * 
 */

/**
 * @author 75659
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String search(String keyword, @RequestParam(defaultValue="1") Integer page, Model model) throws Exception{
		//
		keyword = new String(keyword.getBytes("iso8859-1"),"utf-8");
		SearchResult result = searchService.search(keyword,page,PAGE_ROWS);
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", result.getRecordCount());
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
}
