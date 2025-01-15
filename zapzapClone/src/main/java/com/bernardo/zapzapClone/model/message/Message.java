package com.bernardo.zapzapClone.model.message;


import com.bernardo.zapzapClone.model.baseEntity.BaseAuditingEntity;
import com.bernardo.zapzapClone.model.chat.Chat;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
@NamedQuery(name = "Message.findByChatId", query = "SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.createdAt ASC")
@NamedQuery(name = "Message.findByChatIdAndUserId", query = "SELECT m FROM Message m WHERE m.chat.id = :chatId AND (m.senderId = :userId OR m.receiverId = :userId) ORDER BY m.createdAt ASC")
@NamedQuery(name = "Message.setMessagesToSeenByChatId", query = "UPDATE Message m SET m.state = MessageState.SEEN WHERE m.chat.id = :chatId")
@NamedQuery(name = "Message.setMessagesToReceivedByChatId", query = "UPDATE Message m SET m.state = MessageState.RECEIVED WHERE m.chat.id = :chatId")
@NamedQuery(name = "Message.setMessageToStateByChatId", query = "UPDATE Message m SET m.state = :state WHERE m.chat.id = :chatId ")

public class Message extends BaseAuditingEntity {

    @Id
    @SequenceGenerator(name = "msg_seq", sequenceName = "msg_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_seq")
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageState state;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @Column(nullable = false)
    private String senderId;
    @Column(nullable = false)
    private String receiverId;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @Transient
    public String mediaFilePath;
}
