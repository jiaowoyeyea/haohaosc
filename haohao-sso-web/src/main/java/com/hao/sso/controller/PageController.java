package com.hao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/page/login")
	public String showLogin(String redirectUrl, Model model){
		model.addAttribute("redirect", redirectUrl);
		return "login";
	}
}
