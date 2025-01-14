package com.bernardo.zapzapClone.model.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, String> {
    
    
    
    @Query(name="Chat.findChatsByUserId")
    List<Chat> findChatsByUserId(String userId);
    @Query(name="Chat.findChatByUsersId")
    Optional<Chat> findChatByUsersId(String senderId, String receiverId);




}
