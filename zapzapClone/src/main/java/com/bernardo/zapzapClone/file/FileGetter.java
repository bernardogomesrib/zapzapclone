package com.bernardo.zapzapClone.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.bernardo.zapzapClone.model.message.MessageType;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class FileGetter {
    private FileGetter(){
        throw new IllegalStateException("Utility class");
    }
    public static byte[] getFile(String path){
        if(path == null || path.isEmpty()|| path.isBlank()){
            return new byte[0];
        }   
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            log.warn("Error reading file: {}", path);
        }
        return new byte[0];
    }

    public static byte[] getNotificationIcon(MessageType messageType, String path){
        if(messageType == MessageType.TEXT){
            return new byte[0];
        }
        if (messageType == MessageType.IMAGE){
            return getFile(path);
            
        }
        if(messageType == MessageType.AUDIO){
            return getFile("/public/icons/music_note.svg");
        }
        if(messageType == MessageType.VIDEO){
            return getFile("/public/icons/video.svg");
        }
        if(messageType == MessageType.DOCUMENT){
            return getFile("/public/icons/document.svg");
        }

        return getFile(path);
    }
}
