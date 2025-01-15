package com.bernardo.zapzapClone.notification;

import com.bernardo.zapzapClone.model.message.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private String chatId;
    private String senderId;
    private String senderName;
    private String message;
    private String receverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType notificationType;
    private byte[] media;
}
