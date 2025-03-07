import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import {
  ChatResponse,
  UserResponse,
  ChatRequest,
} from '../../api-services/models';
import { CommonModule } from '@angular/common';
import { ChatService, UserService } from '../../api-services/services';

@Component({
  selector: 'app-chat-list',
  imports: [CommonModule],
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.scss',
})
export class ChatListComponent implements OnInit {
  @Input() chats: ChatResponse[] = []; // Receber os chats como input
  searchNewContact = false;
  contacts: Array<UserResponse> = [];

  @Output() chatSelected = new EventEmitter<ChatResponse>();

  constructor(
    private userService: UserService,
    private chatService: ChatService
  ) {}

  ngOnInit() {
    // Inscreva-se no evento emitido pelo MainComponent
    // Não é necessário, pois os chats serão passados como input
  }

  searchContact() {
    this.searchNewContact = true;
    this.searchContactList();
  }

  wrapMessage(lastMessage: string | undefined) {
    if (lastMessage && lastMessage.length > 20) {
      return lastMessage.substring(0, 20) + '...';
    } else {
      return lastMessage;
    }
  }

  chatClicked(chat: ChatResponse) {
    this.chatSelected.emit(chat);
  }

  searchContactList() {
    this.userService.getAllUsersButMe(this.searchContact).subscribe({
      next: (response) => {
        this.contacts = response;
      },
    });
  }

  criarChat(user: UserResponse) {
    const chatRequest: ChatRequest = { receiverId: user.id };
    this.chatService.postChat({ body: chatRequest }).subscribe({
      next: (response) => {
        this.chatSelected.emit(response);
      },
    });
  }

  imprimirChats() {
    console.log(this.chats);
  }
  dataParaString(data: string|undefined): string {
    try {
      if (!data) {
        return "Sem data";
      }
      const dataObj = new Date(data);
      return dataObj.getDay() + '/' + dataObj.getMonth() + '/' + dataObj.getFullYear() + ' ' + dataObj.getHours() + ':' + dataObj.getMinutes();
    } catch (error) {
      return "Erro ao converter data";
    }
  }
}
