package com.hao.content.service;

import java.util.List;

import com.hao.pojo.TbContent;

import hao.common.utils.EasyUIResult;
import hao.common.utils.EasyUITreeNode;
import hao.common.utils.HaohaoResult;

public interface ContentService {
	
	/**
	 * 获取内容节点
	 * @return
	 */
	List<EasyUITreeNode> getContentCategoryNode(Long parentId);
	
	/**
	 * 分页获取内容信息列表
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIResult getContentCategoryTreeList(Long categoryId, Integer page, Integer rows);
	
	/**
	 * 添加内容
	 * @param content
	 * @return
	 */
	HaohaoResult saveContent(TbContent content);
	
	/**
	 * 编辑内容
	 * @param content
	 * @return
	 */
	HaohaoResult updateContent(TbContent content);

	/**
	 * 删除内容
	 * @param ids
	 * @return
	 */
	HaohaoResult deleteContent(String ids);
	
	/**
	 * 获取首页轮播图
	 * @return
	 */
	List<TbContent> getContentListBy89(Long CONTENT_LUNBO_ID);

}
