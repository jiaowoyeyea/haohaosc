/**
 * 
 */
package com.hao.coro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hao.coro.jedis.JedisClient;
import com.hao.coro.mapper.TbCartMapper;
import com.hao.coro.pojo.TbCart;
import com.hao.coro.pojo.TbCartExample;
import com.hao.coro.pojo.TbCartExample.Criteria;
import com.hao.coro.service.CartService;
import com.hao.pojo.TbItem;
import com.hao.service.ItemService;

import hao.common.utils.JsonUtils;

/**
 * @author 75659
 *
 */
@Service
public class CartServiceImpl implements CartService {
	
	@Value("${CART_BEAN_KEY}")
	private String CART_BEAN_KEY;
	@Value("${CART_SECONDS}")
	private Integer CART_SECONDS;
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private ItemService itemService;
	@Autowired
	private TbCartMapper cartMapper;

	@Override
	public void addItemToCart(String cartKey, Long itemId, Integer num, Long userId) {
		// 
		String key = CART_BEAN_KEY + ":" + cartKey;
		String json = jedisClient.hget(key, itemId.toString());
		
		TbCart cart = null;
		if(StringUtils.isBlank(json)){
			//商品不存在
			TbItem tbItem = itemService.getItemById(itemId);
			cart = new TbCart();
			cart.setItemId(itemId);
			cart.setItemImage(tbItem.getImage());
			cart.setItemPrice(tbItem.getPrice());
			cart.setItemTitle(tbItem.getTitle());
			cart.setNum(num);
			
			Date date = new Date();
			cart.setUpdated(date);
			cart.setCreated(date);
		}else{
			
			cart = JsonUtils.jsonToPojo(json, TbCart.class);
			cart.setNum(cart.getNum()+num);
			cart.setUpdated(new Date());
		}
		
		jedisClient.hset(key, itemId.toString(), JsonUtils.objectToJson(cart));
		if(userId != null){
			jedisClient.hset(key, "user", userId.toString());
		}
		jedisClient.expire(key, CART_SECONDS);
	}

	@Override
	public List<TbCart> getCartList(String cartKey) {
		// 
		String key = CART_BEAN_KEY + ":" + cartKey;
		
		Map<String, String> hgetAll = jedisClient.hgetAll(key);
		
		List<TbCart> cartList = new ArrayList<TbCart>(hgetAll.size());
		
		for (String value : hgetAll.values()) {
			cartList.add(JsonUtils.jsonToPojo(value, TbCart.class));
		}
		
		jedisClient.expire(key, CART_SECONDS);
		return cartList;
	}

	@Override
	public void deleteItem(String cartKey, Long itemId) {
		// 
		String key = CART_BEAN_KEY + ":" + cartKey;
		jedisClient.hdel(key, itemId.toString());
		jedisClient.expire(key, CART_SECONDS);
	}

	@Override
	public void caeateCart(TbCart cart) {
		// 
		cartMapper.insert(cart);
		
	}

	@Override
	public void addCart(Long itemId,Integer num, Long id) {
		//数据库购物车添加商品
		TbCartExample example = new TbCartExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		
		List<TbCart> list = cartMapper.selectByExample(example);
		for (TbCart tbCart : list) {
			if(Long.valueOf(tbCart.getItemId()) == itemId){
				tbCart.setNum(tbCart.getNum() + num);
				tbCart.setUpdated(new Date());
				cartMapper.updateByPrimaryKeySelective(tbCart);
				break;
			}
		}
		
	}

	@Override
	public List<TbCart> selectCartList(Long id) {
		// 
		TbCartExample example = new TbCartExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		
		List<TbCart> list = cartMapper.selectByExample(example);
		return list;
	}

}
