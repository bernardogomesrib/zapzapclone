package com.bernardo.zapzapClone.model.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bernardo.zapzapClone.model.user.UserMapper;

@Service
public class ChatConverter {
    @Autowired
    private UserMapper userMapper;
    
    public ChatResponse convert(Chat chat,String userId) {
        return ChatResponse.builder()
            .id(chat.getId())
            .senderId(chat.getSender().getId())
            .receiverId(chat.getReceiver().getId())
            .messages(chat.getMessages())
            .chatName(chat.getChatName(userId))
            .unreadMessagesCount(chat.getUnreadMessagesCount(userId))
            .lastMessage(chat.getLastMessage())
            .lastMessageTime(chat.getLastMessageTime())
            .isReciverOnline(chat.getReceiver().isOnline())
            .users(List.of(userMapper.toUserResponse(chat.getSender()),userMapper.toUserResponse(chat.getReceiver())))
            .build();
    }


    public List<ChatResponse> convert(List<Chat> chatsByUserId,String userId) {
        return chatsByUserId.stream().map(chat -> convert(chat, userId)).toList();
    }
}
