/**
 * 
 */
package com.hao.coro.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hao.coro.pojo.TbCart;
import com.hao.coro.service.CartService;
import com.hao.pojo.TbUser;

import hao.common.utils.CookieUtils;
import hao.common.utils.ExceptionUtil;
import hao.common.utils.HaohaoResult;

/**
 * @author 75659
 *
 */
@Controller
public class CartController {
	
	//604800
	/*@Autowired
	private ItemService itemService;*/
	@Autowired
	private CartService cartService;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	private static final String HH_CART = "HH_CART";
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@CookieValue(value = HH_CART, required = false) String cartKey, @PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
		TbUser user = (TbUser) request.getAttribute("user");
		if(StringUtils.isBlank(cartKey)){
			cartKey = DigestUtils.md5Hex(itemId+""+System.currentTimeMillis());
		}
		if(user == null){
			try {
				cartService.addItemToCart(cartKey, itemId, num, null);
				CookieUtils.setCookie(request, response, HH_CART, cartKey, CART_EXPIRE);
				return "cartSuccess";
			} catch (Exception e) {
				e.printStackTrace();
				//ExceptionUtil.getStackTrace(e);老哥凉凉
			}
		}else{
			
			/*cartService.addItemToCart(cartKey, itemId, num, user.getId());
			CookieUtils.setCookie(request, response, HH_CART, cartKey, CART_EXPIRE);
			return "cartSuccess";*/
			cartService.addCart(itemId, num, user.getId());
			CookieUtils.setCookie(request, response, HH_CART, cartKey, CART_EXPIRE);
			return "cartSuccess";
			
			//没有错误页面不写
		}
			
		return null;
	}
	
	/**
	 * 显示购物车
	 * @param cartKey
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(@CookieValue(HH_CART) String cartKey, Model model, HttpServletRequest request, HttpServletResponse response){
		if(StringUtils.isBlank(cartKey)){
			return "cart";
		}
		List<TbCart> cartList = cartService.getCartList(cartKey);
		model.addAttribute("cartList", cartList);
		CookieUtils.setCookie(request, response, HH_CART, cartKey, CART_EXPIRE);
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public HaohaoResult updateNum(@PathVariable Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
		String cartKey = CookieUtils.getCookieValue(request, HH_CART);
		
		try {
			cartService.addItemToCart(cartKey, itemId, num, null);
			CookieUtils.setCookie(request, response, HH_CART, cartKey, CART_EXPIRE);
			return HaohaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return HaohaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@CookieValue(HH_CART) String cartKey, @PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response){
		if(itemId == null){
			return "redirect:/cart/cart.html";
		}
		cartService.deleteItem(cartKey, itemId);
		return "redirect:/cart/cart.html";
	}
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//登陆才能看购物车，直接查数据库
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbCart> cartList = cartService.selectCartList(user.getId());
		request.setAttribute("cartList", cartList);
		return "order-cart";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
