/**
 * 
 */
package com.hao.coro.service;

import java.util.List;

import com.hao.coro.pojo.TbCart;

/**
 * @author 75659
 *
 */
public interface CartService {
	
	/**
	 * 添加购物车
	 * @param cartKey
	 * @param itemId
	 * @param num
	 * @param userId 
	 * @return
	 */
	void addItemToCart(String cartKey, Long itemId, Integer num, Long userId);
	
	/**
	 * 显示购物车
	 * @param hhCart
	 * @return
	 */
	List<TbCart> getCartList(String cartKey);
	
	/**
	 * 删除商品
	 * @param cartKey
	 * @param itemId
	 */
	void deleteItem(String cartKey, Long itemId);
	
	/**
	 * 购物车同步（数据库）
	 * @param cart
	 */
	void caeateCart(TbCart cart);
	
	/**
	 * 数据库购物车添加商品
	 * @param itemId
	 * @param id
	 */
	void addCart(Long itemId,Integer num, Long id);
	
	/**
	 * 查询数据库中的购物车
	 * @param id
	 * @return
	 */
	List<TbCart> selectCartList(Long id);

}
