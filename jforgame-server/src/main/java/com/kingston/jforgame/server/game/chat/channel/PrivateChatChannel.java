package com.kingston.jforgame.server.game.chat.channel;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.chat.message.ResPrivateChat;
import com.kingston.jforgame.server.game.chat.model.BaseChatMessage;
import com.kingston.jforgame.server.game.chat.model.TextChatMessage;
import com.kingston.jforgame.server.game.core.MessagePusher;

public class PrivateChatChannel extends ChatChannel {

	@Override
	public ChannelType getChannelType() {
		return ChannelType.PRIVATE;
	}
	
	@Override
	public boolean verifySend(BaseChatMessage message) {
		return true;
	}
	
	@Override
	public void doSend(BaseChatMessage message) {
		TextChatMessage textMessage = (TextChatMessage)message;
		long receiverId = message.getReceiverId();
        Account receiver = GameContext.getAccountManager().get(receiverId);
		
		ResPrivateChat targetNotify = new ResPrivateChat();
		targetNotify.setSenderId(message.getSenderId());
		targetNotify.setContent("我很好");
		MessagePusher.pushMessage(receiverId, targetNotify);
	}

}
