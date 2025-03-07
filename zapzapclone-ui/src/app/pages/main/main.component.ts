import { Component, EventEmitter, OnDestroy, OnInit } from '@angular/core';
import { ChatListComponent } from '../../components/chat-list/chat-list.component';
import {
  ChatResponse,
  Message,
  MessageRequest,
  MessageResponse,
} from '../../api-services/models';
import { ChatService, MessageService } from '../../api-services/services';
import { KeycloakService } from '../../../utils/keycloak/keycloak.service';
import { CommonModule } from '@angular/common';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { FormsModule } from '@angular/forms';
import { EmojiData } from '@ctrl/ngx-emoji-mart/ngx-emoji';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { NotificationType } from './notification';
@Component({
  selector: 'app-main',
  imports: [ChatListComponent, CommonModule, PickerComponent, FormsModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent implements OnInit, OnDestroy {
  private notificationSubscription: any;
  escape($event: KeyboardEvent) {
    if ($event.key === 'Escape') {
      this.emojis = false;
    }
  }
  ngOnDestroy(): void {
    if(this.socketClient!==null){
      this.socketClient.disconnect();
      this.notificationSubscription.unsubscribe();
      this.socketClient=null;

    }
  }
  sendMessage() {
    const message: MessageRequest = {
      chatId: this.selectedChat.id as string,
      content: this.messageContent,
      receiverId: this.selectedChat.users?.filter(
        (user) => user.id !== this.keycloakService.userId
      )[0].id as string,
      type: 'TEXT',
    };
    this.messageService.postMessage({ body: message }).subscribe({
      next: (response) => {
        this.selectedChat.messages?.push(response);
        this.messageContent = '';
        this.selectedChat.lastMessage = response.content;
        this.selectedChat.lastMessageTime = response.createdAt;
        this.emojis = false;
      },
    });
  }
  onClick() {
    this.messageService
      .patchStatusChange({
        chatId: this.selectedChat.id as string,
        status: 'SEEN',
      })
      .subscribe({});
  }
  keyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.sendMessage();
    }
    if (event.key === 'Escape') {
      this.emojis = false;
    }
  }
  emojis: boolean = false;
  selectedChat: ChatResponse = {};
  chats: ChatResponse[] = [];
  chatEmissor = new EventEmitter<ChatResponse[]>(); // Adicionado
  messageContent: string = '';
  socketClient: any = null;
  constructor(
    private chatService: ChatService,
    private keycloakService: KeycloakService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getAllChats();
    this.initWebsocket();
  }

  private getAllChats() {
    this.chatService.getAllChats().subscribe({
      next: (response) => {
        this.chats = response;
        this.chatEmissor.emit(this.chats); // Emitir os chats
      },
    });
  }

  chatSelected($event: ChatResponse) {
    this.selectedChat = $event;
    this.getAllChatMessages(this.selectedChat.id as string);
    this.setMessagesAsReceved(this.selectedChat.id as string);
    this.selectedChat.unreadMessagesCount = 0;
  }

  settings() {
    this.keycloakService.accountManagement();
  }

  logout() {
    this.keycloakService.logout();
  }

  getAllChatMessages(id: string) {
    this.messageService.getMessagesByChat({ chatId: id }).subscribe({
      next: (response) => {
        this.selectedChat.messages = response;
      },
    });
  }

  setMessagesAsReceved(id: string) {
    this.messageService
      .patchStatusChange({ chatId: id, status: 'RECEIVED' })
      .subscribe({});
  }

  messageIsMine(message: Message) {
    return message.senderId === this.keycloakService.userId;
  }

  printChats() {
    console.log(this.chats);
  }
  getUltimaVesVisto() {
    if (this.selectedChat.users) {
      const usuarios = this.selectedChat.users;
      for (let i = 0; i < usuarios.length; i++) {
        if (usuarios[i].id !== this.keycloakService.userId) {
          const data = new Date(usuarios[i].lastSeenAt);
          if (data.getDate() === new Date().getDate()) {
            return (
              'Visto por último hoje às ' +
              data.getHours() +
              ':' +
              data.getMinutes()
            );
          } else {
            return (
              'Visto por último em ' +
              data.getDate() +
              '/' +
              data.getMonth() +
              '/' +
              data.getFullYear() +
              'as' +
              data.getHours() +
              ':' +
              data.getMinutes()
            );
          }
        }
      }
      return 'hehe alguma coisa deu errado na função getUltimaVesVisto';
    } else {
      return 'não sei como chegou nesta parte da função getUltimaVesVisto, mas não tem usuarios no chat selecionado';
    }
  }

  uploadMedia($event: Event) {
    throw new Error('Method not implemented.');
  }
  addEmoji(emoji: any) {
    const emojiSelecionado: EmojiData = emoji.emoji;
    this.messageContent += emojiSelecionado.native;
  }
  dataDeMensagem(message: MessageResponse) {
    try {
      const data = new Date(message.createdAt);
      if (data.getDate() === new Date().getDate()) {
        return data.getHours() + ':' + data.getMinutes();
      } else {
        return data.getDate() + '/' + data.getMonth() + '/' + data.getFullYear();
      }
    } catch (error) {
      return 'Erro ao obter a data da mensagem';

    }
  }
  initWebsocket() {
    if (this.keycloakService.keycloak.tokenParsed?.sub) {
      let ws = new SockJS('http://localhost:8080/ws');
      this.socketClient = Stomp.over(ws);
      const suburl = '/user/' + this.keycloakService.keycloak.tokenParsed.sub + '/chat';
      this.socketClient.connect(
        { Authorization: 'Bearer ' + this.keycloakService.keycloak.token },
        () => {
          this.notificationSubscription = this.socketClient.subscribe(
            suburl,
            (message:any) => {
              const notification: NotificationType = JSON.parse(message.body);
              this.handleNotification(notification);

            },()=>{
              console.log('Erro ao conectar ao websocket');
            }
          );
        }
      );
    }
  }
  handleNotification(notification:NotificationType){
    if(!notification)return;
    if(notification.chatId===this.selectedChat.id){
      switch(notification.notificationType){
        case 'MESSAGE':
          this.selectedChat.lastMessage = notification.message;

          break;
        case 'IMAGE':
          this.selectedChat.lastMessage = 'Imagem';
          break;
        case 'AUDIO':
          this.selectedChat.lastMessage = 'Áudio';
          break;
        case 'DOCUMENT':
          this.selectedChat.lastMessage = 'Documento';
          break;
        case 'VIDEO':
          this.selectedChat.lastMessage = 'Vídeo';
          break;
        case 'SEEN':

          this.selectedChat.messages?.forEach((message)=>{
            if(message.state==='RECEIVED'){
              message.state='SEEN';
            }
          }
          );
          break;
      }
      this.getAllChatMessages(notification.chatId);
      this.setMessagesAsReceved(notification.chatId);
    }else{
      let chatFound = false;
      this.chats.forEach((chat) => {
        if (chat.id === notification.chatId) {
          chatFound = true;
          switch (notification.notificationType) {
        case 'MESSAGE':
          chat.lastMessage = notification.message;
          if(chat.unreadMessagesCount){
            chat.unreadMessagesCount++;
          }
          break;
        case 'IMAGE':
          chat.lastMessage = 'Imagem';
          break;
        case 'AUDIO':
          chat.lastMessage = 'Áudio';
          break;
        case 'DOCUMENT':
          chat.lastMessage = 'Documento';
          break;
        case 'VIDEO':
          chat.lastMessage = 'Vídeo';
          break;
        case 'SEEN':
          chat.messages?.forEach((message) => {
            message.state = 'SEEN';
          });
          break;
          }
        }
      });

      if (!chatFound) {
        const newMessage: MessageResponse = {
          chat: {
            id: notification.chatId,
          },
          content: notification.message,
          createdAt: new Date().toISOString(),
          id: notification.messageId,
          receiverId: notification.receverId,
          senderId: notification.senderId,
          state: 'RECEIVED',
          type: notification.messageType,
        }
        const newChat: ChatResponse = {
          id: notification.chatId,
          lastMessage: notification.message,
          messages: [newMessage],
          users: [],
          unreadMessagesCount: 1,
        };
        this.chats.push(newChat);
      }
    }
  }
}
