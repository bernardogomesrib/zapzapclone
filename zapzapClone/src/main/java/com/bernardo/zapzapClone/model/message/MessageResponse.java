package com.bernardo.zapzapClone.model.message;

import java.time.LocalDateTime;

import com.bernardo.zapzapClone.model.chat.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String content;
    private MessageState state;
    private MessageType type;
    private String senderId;
    private String receiverId;
    private Chat chat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private byte[] file;
}
