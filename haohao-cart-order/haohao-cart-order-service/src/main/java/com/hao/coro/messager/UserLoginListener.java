/**
 * 
 */
package com.hao.coro.messager;

import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.hao.coro.jedis.JedisClient;
import com.hao.coro.pojo.TbCart;
import com.hao.coro.service.CartService;

import hao.common.utils.JsonUtils;

/**
 * @author 75659
 *
 */
public class UserLoginListener implements MessageListener {
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private CartService cartService;
	
	@Override
	public void onMessage(Message message) {
		// 购物车同步
		if(message instanceof TextMessage){
			TextMessage text = (TextMessage) message;
			try {
				String json = text.getText();
				Map map = JsonUtils.jsonToPojo(json, Map.class);
				
				Integer userId = (Integer) map.get("userId");
				String laoge = map.get("HH_CART").toString();//省事写死
				
				String key = "CART_BEAN" + ":" + laoge;
				
				Map<String, String> hgetAll = jedisClient.hgetAll(key);
				
				for (Map.Entry<String, String> entry : hgetAll.entrySet()) {
					if("user".equals(entry.getKey())){
						continue;
					}
					TbCart cart = JsonUtils.jsonToPojo(entry.getValue(), TbCart.class);
					cart.setUserId(userId.longValue());
					//持久化到数据库
					
					cartService.caeateCart(cart);
				}
				//删除redis中的购物车
				jedisClient.del(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
