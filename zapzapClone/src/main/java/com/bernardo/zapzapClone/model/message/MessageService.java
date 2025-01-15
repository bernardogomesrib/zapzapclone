package com.bernardo.zapzapClone.model.message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bernardo.zapzapClone.file.FileService;
import com.bernardo.zapzapClone.model.chat.Chat;
import com.bernardo.zapzapClone.model.chat.ChatRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final FileService fileService;
    private final ContentTypeReturner contentTypeReturner;

    @Transactional
    public Message createMessage(MessageRequest message) {
        Optional<Chat> chat = chatRepository.findById(message.getChatId());
        if (chat.isEmpty()) {
            throw new EntityNotFoundException("Chat not found");
        }
        Message msg = message.build();
        msg.setChat(chat.get());
        msg.setState(MessageState.SENT);
        return messageRepository.save(msg);

        // TODO: notification
    }


    public List<MessageResponse> getMessagesByChatId(String chatId, Authentication authentication) {

        return messageRepository.findByChatIdAndUserId(chatId,authentication.getName()).stream().map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getMessagesByChatId(String chatId) {
        return messageRepository.findMessagesByChatId(chatId).stream().map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMessageState(String chatId, MessageState state, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        // final String reciverId = getReciverId(chat, authentication); @Transactional
        messageRepository.setMessageToStateByChatId(chat.getId(), state);
        // TODO: notification

    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String senderId = getSenderId(chat, authentication);
        final String reciverId = getReciverId(chat, authentication);
        final String filePath = fileService.saveFile(file, senderId);
        MessageType mediaType = contentTypeReturner.getMediaType(file);

        Message message = Message.builder()
                .content(filePath)
                .senderId(senderId)
                .receiverId(reciverId)
                .type(mediaType)
                .state(MessageState.SENT)
                .mediaFilePath(filePath)
                .chat(chat)
                .build();
        messageRepository.save(message);
    }

    public String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getReceiver().getId();
    }

    public String getReciverId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getReceiver().getId();
        }
        return chat.getSender().getId();
    }
}
