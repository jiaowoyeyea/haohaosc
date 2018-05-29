/**
 * 
 */
package com.hao.sso.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.hao.mapper.TbUserMapper;
import com.hao.pojo.TbUser;
import com.hao.pojo.TbUserExample;
import com.hao.pojo.TbUserExample.Criteria;
import com.hao.redis.JedisClient;
import com.hao.sso.service.UserService;

import hao.common.utils.HaohaoResult;
import hao.common.utils.JsonUtils;

/**
 * @author 75659
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination queueDestination;
	
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	/* （非 Javadoc）
	 * @see com.hao.sso.service.UserService#checkData(java.lang.String, java.lang.Integer)
	 */
	@Override
	public HaohaoResult checkData(String param, Integer type) {
		// 
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(type == 1){
			criteria.andUsernameEqualTo(param);
		}else if(type == 2){
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			criteria.andEmailEqualTo(param);
		}else{
			return HaohaoResult.build(400, "老哥参数非法！");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		
		if(list == null || list.size() == 0){
			return HaohaoResult.ok(true);
		}
		return HaohaoResult.ok(false);
	}

	@Override
	public HaohaoResult createUser(TbUser user) {
		// 
		if(StringUtils.isBlank(user.getUsername())){
			return HaohaoResult.build(400, "用户名不能为空！");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return HaohaoResult.build(400, "密码不能为空！");
		}
		if(StringUtils.isNotBlank(user.getEmail())){
			HaohaoResult result = checkData(user.getEmail(), 3);
			if(!(boolean) result.getData()){
				return HaohaoResult.build(400, "邮箱已被使用！");
			}
		}
		if(StringUtils.isNotBlank(user.getPhone())){
			HaohaoResult result = checkData(user.getPhone(), 2);
			if(!(boolean) result.getData()){
				return HaohaoResult.build(400, "电话号码已被使用！");
			}
		}
		if(StringUtils.isNotBlank(user.getUsername())){
			HaohaoResult result = checkData(user.getUsername(), 1);
			if(!(boolean) result.getData()){
				return HaohaoResult.build(400, "用户名已被使用！");
			}
		}
		String password = user.getPassword();
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		user.setPassword(password);
		
		Date date = new Date();
		user.setCreated(date);
		user.setUpdated(date);
		
		userMapper.insert(user);
		
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult login(String username, String password, final String cartKey) {
		// 
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		
		if(list == null || list.size() == 0){
			return HaohaoResult.build(400, "用户名或密码错误！");
		}
		final TbUser user = list.get(0);
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
			return HaohaoResult.build(400, "用户名或密码错误！");
		}
		//登陆成功生产token
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
		jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
		
		if(cartKey != null){
			
			jmsTemplate.send(queueDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// 
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("HH_CART", cartKey);
					map.put("userId", user.getId());
					TextMessage message = session.createTextMessage(JsonUtils.objectToJson(map));//写死了
					return message;
				}
			});
		}
		
		return HaohaoResult.ok(token);
	}

	@Override
	public HaohaoResult getUserByToken(String token) {
		// 
		String json = jedisClient.get(USER_INFO + ":" + token);
		if(StringUtils.isBlank(json)){
			return HaohaoResult.build(400, "登陆过期！需要从新登陆！");
		}
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
		return HaohaoResult.ok(user);
	}

	@Override
	public HaohaoResult logout(String token) {
		// 
		/*String json = jedisClient.get(USER_INFO + ":" + token);
		if(StringUtils.isBlank(json)){
			return HaohaoResult.ok();
		}*/
		jedisClient.del(USER_INFO + ":" + token);
		return HaohaoResult.ok();
	}

	
	
	//TODO 登陆监听器没写
	
	
	
}
