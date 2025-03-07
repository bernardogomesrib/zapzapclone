import { Message } from "../../api-services/models";

export interface NotificationType {
  chatId: string;
  senderId: string;
  senderName: string;
  message?: string;
  receverId: string;
  chatName?: string;
  messageType?: 'TEXT' | 'IMAGE' | 'AUDIO' | 'DOCUMENT' | 'VIDEO';
  notificationType:
    | 'SEEN'
    | 'MESSAGE'
    | 'IMAGE'
    | 'AUDIO'
    | 'DOCUMENT'
    | 'VIDEO';
  media?: Array<string>;
  messageId?:number;
}
/*

 .chatId(chat.getId())

                .senderId(getSenderId(chat, authentication))
                .receverId(reciverId)
                .notificationType(NotificationType.SEEN)
    private String chatId;
    private String senderId;
    private String senderName;
    private String message;
    private String receverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType notificationType;
    private byte[] media; */
