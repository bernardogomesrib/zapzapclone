package com.bernardo.zapzapClone.model.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(name = "Message.findByChatId")
    List<Message> findMessagesByChatId(String chatId);
    @Query(name="Message.setMessagesToSeenByChatId")
    void setMessagesToSeenByChatId(String chatId);

    @Query(name="Message.setMessageToStateByChatId")
    @Modifying
    void setMessageToStateByChatId(String chatId, MessageState state);
} 
