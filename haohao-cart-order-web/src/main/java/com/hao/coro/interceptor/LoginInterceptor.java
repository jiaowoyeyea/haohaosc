/**
 * 
 */
package com.hao.coro.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hao.sso.service.UserService;

import hao.common.utils.CookieUtils;
import hao.common.utils.HaohaoResult;

/**
 * @author 75659
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	
	@Value("${COOKEI_TOKEN_KEY}")
	private String COOKEI_TOKEN_KEY;
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Autowired
	private UserService userService;
	
	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {
		// 

	}

	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView arg3)
			throws Exception {
		// 

	}

	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 前
		String token = CookieUtils.getCookieValue(request, COOKEI_TOKEN_KEY);
		if(StringUtils.isBlank(token)){
			String url = request.getRequestURL().toString();
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			return false;
		}
		
		HaohaoResult result = userService.getUserByToken(token);
		if(result.getStatus().intValue() != 200){
			String url = request.getRequestURL().toString();
			response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + url);
			return false;
		}
		request.setAttribute("user", result.getData());
		return true;
	}

}
