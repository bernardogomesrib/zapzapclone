import { Component, OnInit } from '@angular/core';
import { ChatListComponent } from '../../components/chat-list/chat-list.component';
import { ChatResponse } from '../../api-services/models';
import { ChatService, MessageService } from '../../api-services/services';
import { KeycloakService } from '../../../utils/keycloak/keycloak.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-main',
  imports: [ChatListComponent, CommonModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})

export class MainComponent implements OnInit {
chatSelected($event: ChatResponse) {
  this.selectedChat = $event;
  this.getAllChatMessages(this.selectedChat.id as string);
  this.setMessagesAsReceved(this.selectedChat.id as string);

}
  settings() {
    this.keycloakService.accountManagement();
  }
  logout() {
    this.keycloakService.logout();
  }
  selectedChat: ChatResponse= {};
  chats: Array<ChatResponse> = [];
  constructor(
    private chatService: ChatService,
    private keycloakService: KeycloakService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getAllChats();
  }

  private getAllChats() {
    this.chatService.getAllChats().subscribe({
      next: (response) => {
        this.chats = response;
      },
    });
  }
  getAllChatMessages(id: string) {
    this.messageService.getMessagesByChat({ chatId:id }).subscribe({
      next: (response) => {
        this.selectedChat.messages = response;
      },
    });
  }
  setMessagesAsReceved(id: string) {
    this.messageService.patchStatusChange({chatId:id,status:'RECEIVED'}).subscribe({});
  }
}
