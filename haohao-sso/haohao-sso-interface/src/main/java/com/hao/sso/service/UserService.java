package com.hao.sso.service;

import com.hao.pojo.TbUser;

import hao.common.utils.HaohaoResult;

public interface UserService {
	
	/**
	 * 用户注册数据校验
	 * @param param
	 * @param type
	 * @return
	 */
	HaohaoResult checkData(String param, Integer type);
	
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	HaohaoResult createUser(TbUser user);
	
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	HaohaoResult login(String username, String password, String cartKey);
	
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 */
	HaohaoResult getUserByToken(String token);
	
	/**
	 * 用户退出
	 * @param token
	 * @return
	 */
	HaohaoResult logout(String token);

}
