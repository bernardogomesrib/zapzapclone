package com.bernardo.zapzapClone.model.message;

import org.springframework.stereotype.Service;

import com.bernardo.zapzapClone.file.FileGetter;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder().
        id(message.getId())
        .senderId(message.getSenderId())
        .content(message.getContent())
        .state(message.getState())
        .type(message.getType())
        .state(message.getState())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .file(FileGetter.getFile(message.getMediaFilePath()))
        .build();
    }
}
