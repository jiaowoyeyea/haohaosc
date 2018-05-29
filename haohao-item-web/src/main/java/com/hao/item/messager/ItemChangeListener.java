package com.hao.item.messager;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hao.item.pojo.Item;
import com.hao.pojo.TbItem;
import com.hao.pojo.TbItemDesc;
import com.hao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemChangeListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;
	
	@Value("${STATIC_PAGE_PATH}")
	private String STATIC_PAGE_PATH;
	
	@Override
	public void onMessage(Message message) {
		// 稳如老哥页面静态化岂不是美滋滋
		if(message instanceof TextMessage){
			try {
			TextMessage text = (TextMessage) message;
			
				String itemId = text.getText();
				//查询数据
				TbItem tbItem = itemService.getItemById(Long.valueOf(itemId));
				
				Item item = new Item(tbItem);
				
				TbItemDesc itemDesc = (TbItemDesc) itemService.getItemDescById(Long.valueOf(itemId)).getData();
				// 1、从spring容器中获得FreeMarkerConfigurer对象。
				// 2、从FreeMarkerConfigurer对象中获得Configuration对象。
				Configuration configuration = freemarkerConfig.getConfiguration();
				// 3、使用Configuration对象获得Template对象。
				Template template = configuration.getTemplate("item.ftl");
				// 4、创建数据集
				Map<String, Object> dataModel = new HashMap<String, Object>();
				// 5、 封装数据
				dataModel.put("item", item);
				dataModel.put("itemDesc", itemDesc);
				// 6、创建输出文件的Writer对象。
				Writer out = new FileWriter(new File(STATIC_PAGE_PATH + itemId + ".html"));
				// 7、调用模板对象的process方法，生成文件。
				template.process(dataModel, out);
				// 7、关闭流。
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
