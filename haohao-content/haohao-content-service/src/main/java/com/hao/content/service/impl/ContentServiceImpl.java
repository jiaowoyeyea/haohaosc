package com.hao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hao.content.jedis.JedisClient;
import com.hao.content.service.ContentService;
import com.hao.mapper.TbContentCategoryMapper;
import com.hao.mapper.TbContentMapper;
import com.hao.pojo.TbContent;
import com.hao.pojo.TbContentCategory;
import com.hao.pojo.TbContentCategoryExample;
import com.hao.pojo.TbContentCategoryExample.Criteria;
import com.hao.pojo.TbContentExample;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;
import hao.common.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	
	@Override
	public List<EasyUITreeNode> getContentCategoryNode(Long parentId) {
		// 
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
			for (TbContentCategory tbContentCategory : list) {
				EasyUITreeNode node = new EasyUITreeNode();
				node.setId(tbContentCategory.getId());
				node.setState(tbContentCategory.getIsParent()?"closed":"open");
				node.setText(tbContentCategory.getName());
				nodeList.add(node);
			}
			return nodeList;
		}
		return null;
	}

	@Override
	public EasyUIResult getContentCategoryTreeList(Long categoryId, Integer page, Integer rows) {
		// 
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		com.hao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		PageInfo<TbContent> info = new PageInfo<TbContent>(list);
		
		if(list != null && list.size() > 0){
			EasyUIResult result = new EasyUIResult(info.getTotal(), list);
			return result;
		}
		return new EasyUIResult(info.getTotal(),null);
	}

	@Override
	public HaohaoResult saveContent(TbContent content) {
		// 
		Date date = new Date();
		content.setUpdated(date);
		content.setCreated(date);
		contentMapper.insert(content);
		
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return HaohaoResult.ok();
		
	}

	@Override
	public HaohaoResult updateContent(TbContent content) {
		// 
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return HaohaoResult.ok();
	}

	@Override
	public HaohaoResult deleteContent(String ids) {
		// 
		String[] split = ids.split(",");
		TbContent content;
		for (String id : split) {
			
			try {
				content = contentMapper.selectByPrimaryKey(Long.valueOf(id));
				jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			contentMapper.deleteByPrimaryKey(Long.valueOf(id));
		}
		return HaohaoResult.ok();
	}

	@Override
	public List<TbContent> getContentListBy89(Long CONTENT_LUNBO_ID) {
		// 
		try {
			String json = jedisClient.hget(CONTENT_LIST, CONTENT_LUNBO_ID.toString());
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		com.hao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(CONTENT_LUNBO_ID);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		try {
			jedisClient.hset(CONTENT_LIST, CONTENT_LUNBO_ID.toString(), JsonUtils.objectToJson(list));
			System.out.println("老哥添加轮播图缓存了！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
