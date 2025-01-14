package com.bernardo.zapzapClone.model.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.bernardo.zapzapClone.model.user.User;
import com.bernardo.zapzapClone.model.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatConverter chatConverter;
    private final UserRepository userRepository;
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReciverId(Authentication authentication) {
        String userId = authentication.getName();
        return chatConverter.convert(chatRepository.findChatsByUserId(userId), userId);
    }
    public ChatResponse createChat(String senderId, String receiverId) {
        Optional<Chat> chat = chatRepository.findChatByUsersId(senderId, receiverId);
        if (chat.isEmpty()) {
            Chat newChat = new Chat();
            Optional<User> sender = userRepository.findById(senderId);
            if(sender.isEmpty()) {
                throw new EntityNotFoundException("Sender not found");
            }
            Optional<User> receiver = userRepository.findById(receiverId);
            if(receiver.isEmpty()) {
                throw new EntityNotFoundException("Receiver not found");
            }
            newChat.setSender(sender.get());
            newChat.setReceiver(receiver.get());
            chatRepository.save(newChat);
            return chatConverter.convert(newChat, senderId);
        }
        return chatConverter.convert(chat.get(), senderId);
    }
}
