package com.bernardo.zapzapClone.model.chat;

import java.util.List;

import com.bernardo.zapzapClone.model.message.Message;
import com.bernardo.zapzapClone.model.user.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private String id;
    private List<Message> messages;
    private String chatName;
    private long unreadMessagesCount;
    private String lastMessage;
    private String lastMessageTime;
    private String senderId;
    private String receiverId;
    private boolean isReciverOnline;
    private List<UserResponse> users;

}
