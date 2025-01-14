package com.bernardo.zapzapClone.model.user;

import java.time.LocalDateTime;
import java.util.List;

import com.bernardo.zapzapClone.model.baseEntity.BaseAuditingEntity;
import com.bernardo.zapzapClone.model.chat.Chat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u where u.id != :publicId")
@NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
public class User extends BaseAuditingEntity {
    private final static int LAST_ACTIVE_INTERVAL = 1;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime lastSeenAt;
    @JsonIdentityReference
    @OneToMany
    private List<Chat> chatsAsSender;
    @OneToMany
    @JsonIdentityReference
    private List<Chat> chatsAsReceiver;

    @Transient
    public boolean isOnline() {
        return lastSeenAt != null && lastSeenAt.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

}
