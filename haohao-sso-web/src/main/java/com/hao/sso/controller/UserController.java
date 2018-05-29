/**
 * 
 */
package com.hao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.pojo.TbUser;
import com.hao.sso.service.UserService;

import hao.common.utils.CookieUtils;
import hao.common.utils.HaohaoResult;

/**
 * @author 75659
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Value("${COOKEI_TOKEN_KEY}")
	private String COOKEI_TOKEN_KEY;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public HaohaoResult checkData(@PathVariable String param, @PathVariable Integer type){
		
		HaohaoResult result = userService.checkData(param,type);
		return result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult register(TbUser user){
		HaohaoResult result = userService.createUser(user);
		return result;
	}
	
	
	// 1、登录页面提交用户名密码。
	// 2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
	// 3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
	// 4、使用String类型保存Session信息。可以使用“前缀:token”为key
	// 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
	// 6、把token写入cookie中。
	// 7、Cookie需要跨域。例如www.hao.com\sso.hao.com\order.hao.com，可以使用工具类。
	// 8、Cookie的有效期。关闭浏览器失效。
	// 9、登录成功。
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public HaohaoResult login(@CookieValue(value = "HH_CART", required = false)String cartKey, String username, String password, HttpServletRequest request, HttpServletResponse response){
		HaohaoResult result = userService.login(username, password, cartKey);
		
		if(result.getStatus().intValue() == 200){
			String token = (String) result.getData();
			CookieUtils.setCookie(request, response, COOKEI_TOKEN_KEY, token);
			return HaohaoResult.ok(token);
		}
		return result;
	}
	
	/*@RequestMapping("/user/token/{token}")
	@ResponseBody
	public HaohaoResult getUserByToken(@PathVariable String token){
		HaohaoResult result = userService.getUserByToken(token);
		return result;
	}*/
	
	@RequestMapping("/user/logout/{token}")
	@ResponseBody
	public HaohaoResult logout(@PathVariable String token){
		HaohaoResult result = userService.logout(token);
		return result;
	}
	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback){
		HaohaoResult result = userService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
