package com.bernardo.zapzapClone.model.message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bernardo.zapzapClone.file.FileGetter;
import com.bernardo.zapzapClone.file.FileService;
import com.bernardo.zapzapClone.model.chat.Chat;
import com.bernardo.zapzapClone.model.chat.ChatRepository;
import com.bernardo.zapzapClone.notification.Notification;
import com.bernardo.zapzapClone.notification.NotificationService;
import com.bernardo.zapzapClone.notification.NotificationType;

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
    private final NotificationService notificationService;

    @Transactional
    public Message createMessage(MessageRequest message, Authentication authentication) {
        Optional<Chat> chat = chatRepository.findById(message.getChatId());
        if (chat.isEmpty()) {
            throw new EntityNotFoundException("Chat not found");
        }

        Chat c = chat.get();
        Message msg = message.build();
        msg.setChat(chat.get());
        msg.setState(MessageState.SENT);
        Notification notification = Notification.builder()
                .chatId(c.getId())
                .message(message.getContent())
                .senderId(authentication.getName())
                .receverId(message.getReceiverId())
                .chatName(c.getChatName(authentication.getName()))
                .messageType(MessageType.TEXT)
                .notificationType(NotificationType.MESSAGE)
                .build();

        notificationService.sendNotification(message.getReceiverId(), notification);

        return messageRepository.save(msg);
    }

    public List<MessageResponse> getMessagesByChatId(String chatId, Authentication authentication) {

        return messageRepository.findByChatIdAndUserId(chatId, authentication.getName()).stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getMessagesByChatId(String chatId) {
        return messageRepository.findMessagesByChatId(chatId).stream().map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMessageState(String chatId, MessageState state, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String reciverId = getReciverId(chat, authentication);
       

        Notification notification = Notification.builder()
                .chatId(chat.getId())

                .senderId(getSenderId(chat, authentication))
                .receverId(reciverId)
                .notificationType(NotificationType.SEEN)
                .build();

        notificationService.sendNotification(reciverId, notification);

        messageRepository.setMessageToStateByChatId(chat.getId(), state);

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
        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .message(message.getContent())
                .senderId(authentication.getName())
                .receverId(reciverId)
                .chatName(chat.getChatName(authentication.getName()))
                .messageType(mediaType)
                .notificationType(NotificationType.fromMessageType(mediaType))
                .media(FileGetter.getNotificationIcon(mediaType,filePath))
                .build();

        notificationService.sendNotification(reciverId, notification);

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
