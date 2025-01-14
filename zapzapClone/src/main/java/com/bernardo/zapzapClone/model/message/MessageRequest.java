package com.bernardo.zapzapClone.model.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NotNull
    private String content;
    @NotNull
    private String receiverId;
    @NotNull
    private MessageType type;
    @NotNull
    private String chatId;
    
    public Message build() {
        return Message.builder()
                .content(content)
                .receiverId(receiverId)
                .type(type)
                .build();
    }
}
