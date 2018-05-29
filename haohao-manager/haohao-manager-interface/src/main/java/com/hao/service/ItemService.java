/**
 * 
 */
package com.hao.service;

import java.util.List;

import com.hao.pojo.TbItem;
import com.hao.pojo.TbItemParam;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;

/**
 * @author 75659
 *
 */
public interface ItemService {
	
	/**
	 * 分页查询商品数据
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIResult getItemList(Integer page, Integer rows);
	
	/**
	 * 获取商品分类
	 * @return
	 */
	List<EasyUITreeNode> getEasyUITreeNodeList(Long parentId);
	
	/**
	 * 添加商品
	 * @param item
	 * @return
	 */
	HaohaoResult saveItem(TbItem item, String desc, String itemParams);
	
	/**
	 * 分页显示规格列表
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIResult getParamList(Integer page, Integer rows);
	
	/**
	 * 判断是否添加过此类目的规格参数
	 * @param itemcatid
	 * @return
	 */
	HaohaoResult selectParamItemCatExistsById(Long itemcatid);
	
	/**
	 * 添加规格
	 * @param itemcatid
	 * @param itemParam
	 * @return
	 */
	HaohaoResult saveItemParam(Long itemcatid, TbItemParam itemParam);
	
	/**
	 * 根据id删除规格
	 * @param ids
	 * @return
	 */
	HaohaoResult deleteItemParamById(String ids);
	
	/**
	 * 根据Id查询商品规格参数
	 * @param itemId
	 * @return
	 */
	HaohaoResult getItemParamItemById(Long itemId);
	
	/**
	 * 根据Id查询商品描述
	 * @param itemId
	 * @return
	 */
	HaohaoResult getItemDescById(Long itemId);
	
	/**
	 * 修改商品所有信息
	 * @param item
	 * @param desc
	 * @param itemParams
	 * @param itemParamId
	 * @return
	 */
	HaohaoResult updateItemAll(TbItem item, String desc, String itemParams, Long itemParamId);
	
	/**
	 * 商品上架
	 * @param ids
	 * @return
	 */
	HaohaoResult updateItemReshelf(String ids);
	
	/**
	 * 商品下架
	 * @param ids
	 * @return
	 */
	HaohaoResult updateItemInstock(String ids);
	
	/**
	 * 商品删除
	 * @param ids
	 * @return
	 */
	HaohaoResult deleteItemByIds(String ids);
	
	/**
	 * 根据商品id查询商品
	 * @param itemId
	 * @return
	 */
	TbItem getItemById(Long itemId);
	
}
