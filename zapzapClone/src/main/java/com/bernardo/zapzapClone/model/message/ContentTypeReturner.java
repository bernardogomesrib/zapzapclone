package com.bernardo.zapzapClone.model.message;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bernardo.zapzapClone.exception.FileException;

@Service
public class ContentTypeReturner {
    public MessageType getMediaType(MultipartFile file){

        String contentType = file.getContentType();
    
        MessageType mediaType;
        if (contentType != null) {
            switch (contentType) {
                case "image/jpeg":
                case "image/png":
                case "image/gif":
                case "image/bmp":
                case "image/webp":
                    mediaType = MessageType.IMAGE;
                    break;
                case "video/mp4":
                case "video/mpeg":
                case "video/ogg":
                case "video/webm":
                case "video/quicktime":
                    mediaType = MessageType.VIDEO;
                    break;
                case "audio/mpeg":
                case "audio/ogg":
                case "audio/wav":
                case "audio/webm":
                    mediaType = MessageType.AUDIO;
                    break;
                case "application/pdf":
                case "application/msword":
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                case "application/vnd.ms-excel":
                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                case "application/vnd.ms-powerpoint":
                case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                    mediaType = MessageType.DOCUMENT;
                    break;
                default:
                    throw new FileException("Unsupported file type: " + contentType);
            }
        } else {
            throw new FileException("File type is null");
        }
        return mediaType;
    }
}
