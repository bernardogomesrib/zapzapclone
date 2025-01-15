package com.bernardo.zapzapClone.notification;

import com.bernardo.zapzapClone.model.message.MessageType;

public enum NotificationType {
    SEEN,
    MESSAGE,
    IMAGE,AUDIO,DOCUMENT,VIDEO;

    public static NotificationType fromMessageType(MessageType messageType) {
        switch (messageType) {
            case TEXT:
                return MESSAGE;
            case IMAGE:
                return IMAGE;
            case AUDIO:
                return AUDIO;
            case DOCUMENT:
                return DOCUMENT;
            case VIDEO:
                return VIDEO;
            default:
                throw new IllegalArgumentException("Invalid message type");
        }
    }
}
