/**
 * 
 */
package com.hao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hao.mapper.TbItemCatMapper;
import com.hao.mapper.TbItemDescMapper;
import com.hao.mapper.TbItemMapper;
import com.hao.mapper.TbItemParamItemMapper;
import com.hao.mapper.TbItemParamMapper;
import com.hao.pojo.TbItem;
import com.hao.pojo.TbItemCat;
import com.hao.pojo.TbItemCatExample;
import com.hao.pojo.TbItemCatExample.Criteria;
import com.hao.pojo.TbItemDesc;
import com.hao.pojo.TbItemDescExample;
import com.hao.pojo.TbItemExample;
import com.hao.pojo.TbItemParam;
import com.hao.pojo.TbItemParamEnhance;
import com.hao.pojo.TbItemParamExample;
import com.hao.pojo.TbItemParamItem;
import com.hao.pojo.TbItemParamItemExample;
import com.hao.redis.JedisClient;
import com.hao.service.ItemService;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;
import hao.common.utils.IDUtils;
import hao.common.utils.JsonUtils;

/**
 * @author 75659
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper; 
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamMapper itemParamMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${BASE}")
	private String BASE;
	@Value("${DESC}")
	private String DESC;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	
	
	
	
	
	
	
	
	
	
	@Override
	public EasyUIResult getItemList(Integer page, Integer rows) {
		// 分页查询商品信息
		//分页
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> info = new PageInfo<TbItem>(list);
		EasyUIResult result = new EasyUIResult(info.getTotal(),list);
		return result;
	}

	@Override
	public List<EasyUITreeNode> getEasyUITreeNodeList(Long parentId) {
		// 
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		
		return nodeList;
	}

	@Override
	public HaohaoResult saveItem(TbItem item, String desc, String itemParams) {
		// 添加商品
		 final long itemId = IDUtils.getItemId();
		 
		 item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		 item.setStatus((byte) 1);
		 Date date = new Date();
		 item.setCreated(date);
		 item.setUpdated(date);
		 
		 itemMapper.insert(item);
		 
		 TbItemDesc itemDesc = new TbItemDesc();
		 
		 itemDesc.setItemId(itemId);
		 itemDesc.setItemDesc(desc);
		 itemDesc.setCreated(date);
		 itemDesc.setUpdated(date);
		 
		 itemDescMapper.insert(itemDesc);
		 
		 TbItemParamItem paramItem = new TbItemParamItem();
		 paramItem.setItemId(itemId);
		 paramItem.setParamData(itemParams);
		 paramItem.setCreated(date);
		 paramItem.setUpdated(date);
		 
		 itemParamItemMapper.insert(paramItem);
		 
		 
		 jmsTemplate.send(topicDestination, new MessageCreator(){

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 
				return session.createTextMessage(String.valueOf(itemId));
			}
			 
		 });
		 
		return HaohaoResult.ok();
	}

	@Override
	public EasyUIResult getParamList(Integer page, Integer rows) {
		// 
		PageHelper.startPage(page, rows);
		
		List<TbItemParamEnhance> list = itemParamMapper.selectlaoge();
		PageInfo<TbItemParamEnhance> info = new PageInfo<TbItemParamEnhance>(list);
		
	/*	
		int i = 0;
		
		for (TbItemParamEnhance tbItemParamEnhance : list) {
			List<Map> jsonToList = JsonUtils.jsonToList(tbItemParamEnhance.getParamData(), Map.class);
			for (Map map : jsonToList) {
				
				tbItemParamEnhance.setParamData((String) map.get("group"));
				list.set(i, tbItemParamEnhance);
			}
			i ++;
		}*/
		EasyUIResult result = new EasyUIResult(info.getTotal(), list);
		return result;
	}

	@Override
	public HaohaoResult selectParamItemCatExistsById(Long itemcatid) {
		// 
		TbItemParamExample example = new TbItemParamExample();
		com.hao.pojo.TbItemParamExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andItemCatIdEqualTo(itemcatid);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if(list != null && list.size() > 0){
			return HaohaoResult.ok(list.get(0).getParamData());
		}
		return HaohaoResult.build(666, "老哥可用！");
	}

	@Override
	public HaohaoResult saveItemParam(Long itemcatid, TbItemParam itemParam) {
		// 
		itemParam.setItemCatId(itemcatid);
		Date date = new Date();
		itemParam.setCreated(date);
		itemParam.setUpdated(date);
		itemParamMapper.insert(itemParam);
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult deleteItemParamById(String ids) {
		// 
		String[] split = ids.split(",");
		for (int i = 0; i < split.length; i++) {
			
			itemParamMapper.deleteByPrimaryKey(Long.valueOf(split[i]));
		}
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult getItemParamItemById(Long itemId) {
		// 
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.hao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list != null && list.size() > 0){
			for (TbItemParamItem tbItemParamItem : list) {
				
				return HaohaoResult.ok(tbItemParamItem);
			}
		}
		return null;
	}

	@Override
	public HaohaoResult getItemDescById(Long itemId) {
		//
		try {
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":" + DESC);
			if(StringUtils.isNotBlank(json)){
				TbItemDesc pojo = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return HaohaoResult.ok(pojo);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		TbItemDescExample example = new TbItemDescExample();
		
		com.hao.pojo.TbItemDescExample.Criteria criteria = example.createCriteria();
		
		criteria.andItemIdEqualTo(itemId);
		
		List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
		
		if(list != null && list.size() > 0){
			
			for (TbItemDesc tbItemDesc : list) {
				
				try {
					jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":" + DESC, JsonUtils.objectToJson(tbItemDesc));
					
					jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":" + DESC, ITEM_INFO_EXPIRE);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			return HaohaoResult.ok(tbItemDesc);
			
			}
		}
		
		return null;
	}

	@Override
	public HaohaoResult updateItemAll(TbItem item, String desc, String itemParams, Long itemParamId) {
		// 
		Date date = new Date();
		
		// TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
		//
		// item.setUpdated(date);
		// item.setStatus(tbItem.getStatus());
		// item.setCreated(tbItem.getCreated());
		//
		// itemMapper.updateByPrimaryKey(item);
		
		item.setUpdated(date);
		itemMapper.updateByPrimaryKeySelective(item);
		
		if(StringUtils.isNotBlank(desc)){
			TbItemDesc itemDesc = new TbItemDesc();
			itemDesc.setItemId(item.getId());
			itemDesc.setItemDesc(desc);
			itemDesc.setUpdated(date);
			itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		}
		
		if(itemParamId != null){
			TbItemParamItem itemParamItem = new TbItemParamItem();
			itemParamItem.setId(itemParamId);
			itemParamItem.setParamData(itemParams);
			itemParamItem.setUpdated(date);
			itemParamItemMapper.updateByPrimaryKeySelective(itemParamItem);
		}
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult updateItemReshelf(String ids) {
		// 
		String[] split = ids.split(",");
		for (String id : split) {
			TbItem item = new TbItem();
			item.setId(Long.valueOf(id));
			//商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 1);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult updateItemInstock(String ids) {
		// 
		String[] split = ids.split(",");
		for (String id : split) {
			TbItem item = new TbItem();
			item.setId(Long.valueOf(id));
			//商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 2);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult deleteItemByIds(String ids) {
		// 
		String[] split = ids.split(",");
		for (String id : split) {
			TbItem item = new TbItem();
			item.setId(Long.valueOf(id));
			//商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 3);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return HaohaoResult.ok();
	}

	@Override
	public TbItem getItemById(Long itemId) {
		// 
		try {
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":" + BASE);
			if(StringUtils.isNotBlank(json)){
				
				TbItem pojo = JsonUtils.jsonToPojo(json, TbItem.class);
				
				return pojo;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		
		try {
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":" + BASE, JsonUtils.objectToJson(tbItem));
			
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":" + BASE, ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return tbItem;
	}

	
	
	
	
	
	
	
	
	
	
	
}
