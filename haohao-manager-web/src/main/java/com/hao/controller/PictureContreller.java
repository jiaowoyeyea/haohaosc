package com.hao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hao.domain.PictureResult;
import com.hao.utils.FastDFSClient;

import hao.common.utils.JsonUtils;

@Controller
public class PictureContreller {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile){
		//取文件拓展名
		try {
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//创建一个FastDFS客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/client.conf");
			//执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
			//拼接url
			String url = IMAGE_SERVER_URL + path;
			// Map<String, Object> map = new HashMap<>();
			// map.put("error", 0);
			// map.put("url", url);
			
			PictureResult result = new PictureResult();
			result.setError(0);
			result.setUrl(url);
			
			String json = JsonUtils.objectToJson(result);
			return json;
		} catch (Exception e) {
			
			e.printStackTrace();
			Map<String, Object> map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败!");
			
			String json = JsonUtils.objectToJson(map);
			return json;
		}
		
	}
}
