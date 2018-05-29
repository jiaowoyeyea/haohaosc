package com.hao.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hao.content.service.ContentService;
import com.hao.pojo.TbContent;

@Controller
public class IndexContent {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_LUNBO_ID}")
	private Long CONTENT_LUNBO_ID;
	
	
	
	@RequestMapping("/index")
	public String indexShow(Model model){
		List<TbContent> list = contentService.getContentListBy89(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
