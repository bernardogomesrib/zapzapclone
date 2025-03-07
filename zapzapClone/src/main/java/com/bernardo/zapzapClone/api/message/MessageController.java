package com.bernardo.zapzapClone.api.message;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bernardo.zapzapClone.model.message.MessageRequest;
import com.bernardo.zapzapClone.model.message.MessageResponse;
import com.bernardo.zapzapClone.model.message.MessageService;
import com.bernardo.zapzapClone.model.message.MessageState;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Tag(name = "Message")
public class MessageController {
    private final MessageService messageService;
    
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse postMessage(@RequestBody MessageRequest entity,Authentication authentication) {
        return messageService.createMessage(entity, authentication);
    }
    @PostMapping(value = "/upload-media", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse postMessageWithFile(
        @Parameter()
        @RequestParam("file") MultipartFile file,
        @RequestParam("chatId") String chatId,
        Authentication authentication
        ) {
        return messageService.uploadMediaMessage(chatId, file, authentication);
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void patchStatusChange(
        @RequestParam("chatId") String chatId,
        @RequestParam("status") MessageState status,
        Authentication authentication
        ) {
    
        messageService.updateMessageState(chatId, status, authentication);
    }
    @GetMapping("{chatId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByChat(@RequestParam String chatId, Authentication authentication) {
        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, authentication));
    }
    
    
}
