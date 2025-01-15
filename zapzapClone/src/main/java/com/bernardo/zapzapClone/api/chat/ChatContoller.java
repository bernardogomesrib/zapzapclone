package com.bernardo.zapzapClone.api.chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bernardo.zapzapClone.model.chat.ChatResponse;
import com.bernardo.zapzapClone.model.chat.ChatService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatContoller {
    private final ChatService chatService;
    @PostMapping
    public ResponseEntity<ChatResponse> post(Authentication authentication, @RequestBody ChatRequest chatRequest) {
        ChatResponse entity = chatService.createChat(authentication.getName().toString(), chatRequest.getReceiverId());        
        return ResponseEntity.ok(entity);
    }
    @GetMapping()
    public ResponseEntity<List<ChatResponse>> getMethodName(Authentication authentication) {
        return ResponseEntity.ok(chatService.getChatsByReciverId(authentication));
    }
    
    
}
