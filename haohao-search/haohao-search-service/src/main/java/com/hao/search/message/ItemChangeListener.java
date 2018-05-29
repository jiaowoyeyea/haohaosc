package com.hao.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.hao.search.service.IndexService;

public class ItemChangeListener implements MessageListener {
	
	@Autowired
	private IndexService indexService;
	
	@Override
	public void onMessage(Message message) {
		// 
		if(message instanceof TextMessage){
			TextMessage text = (TextMessage) message;
			try {
				Long itemId = Long.parseLong(text.getText());
				indexService.addDocument(itemId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
