package com.bernardo.zapzapClone.model.chat;

import com.bernardo.zapzapClone.model.baseEntity.BaseAuditingEntity;
import com.bernardo.zapzapClone.model.message.Message;
import com.bernardo.zapzapClone.model.message.MessageState;
import com.bernardo.zapzapClone.model.message.MessageType;
import com.bernardo.zapzapClone.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.beans.Transient;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")

@NamedQuery(name = "Chat.findChatsByUserId", query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :userId OR c.receiver.id = :userId ORDER BY c.createdAt DESC")
@NamedQuery(name = "Chat.findChatByUsersId", query = "SELECT c FROM Chat c WHERE (c.sender.id = :senderId AND c.receiver.id = :receiverId) OR (c.sender.id = :receiverId AND c.receiver.id = :senderId)")
public class Chat extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;


    @JsonIdentityReference
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId) {
        return sender.getId().equals(senderId) ? receiver.getFirstName() + " " + receiver.getLastName()
                : sender.getFirstName() + " " + sender.getLastName();
    }

    @Transient
    public long getUnreadMessagesCount(final String userId) {
        return messages.stream()
                .filter(m -> m.getState().equals(MessageState.SENT) && m.getReceiverId().equals(userId))
                .filter(m -> m.getState().equals(MessageState.RECEIVED) && m.getReceiverId().equals(userId)).count();
    }

    @Transient
    public String getLastMessage() {
        if (messages == null || messages.isEmpty()) {
            return null;
        } else {
            if (!messages.get(0).getType().equals(MessageType.TEXT)) {
                return "Attachment";
            } else {
                return messages.get(0).getContent();
            }
        }
    }

    @Transient
    public String getLastMessageTime() {
        if (messages == null || messages.isEmpty()) {
            return null;
        } else {
            return messages.get(0).getCreatedAt().toString();
        }
    }

}
