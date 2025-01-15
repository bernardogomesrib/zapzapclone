import {Component, input, InputSignal, output} from '@angular/core';
import {ChatService} from '../../services/services/chat.service';
import {ChatResponse} from '../../services/models/chat-response';
import {DatePipe} from '@angular/common';
import {UserService} from '../../services/services/user.service';
import {UserResponse} from '../../services/models/user-response';
import {MessageService} from '../../services/services/message.service';
import { KeycloakService } from '../../services/utils/keycloak/keycloak.service';
import { ChatRequest } from '../../services/models';
import { PostChat$Params } from '../../services/fn/chat/post-chat';

@Component({
  selector: 'app-chat-list',
  imports: [DatePipe],
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.scss'
})
export class ChatListComponent {
  chats: InputSignal<ChatResponse[]> = input<ChatResponse[]>([]);
  searchNewContact = false;
  contacts: Array<UserResponse> = [];
  chatSelected = output<ChatResponse>();

  constructor(
    private chatService: ChatService,
    private userService: UserService,
    private keycloakService: KeycloakService
  ) {
  }

  searchContact() {
    this.userService.getAllUsersButMe()
      .subscribe({
        next: (users) => {
          this.contacts = users;
          this.searchNewContact = true;
        }
      });
  }

  selectContact(contact: UserResponse) {
    const chatRequest: ChatRequest = {
        receiverId: contact.id as string
    };

    const params: PostChat$Params = {
        body: chatRequest
    };

    this.chatService.postChat(params).subscribe({
        next: (res) => {
            const chat: ChatResponse = {
                id: res.id,
                chatName: contact.firstName + ' ' + contact.lastName,
                reciverOnline: contact.online,
                lastMessageTime: contact.lastSeenAt,
                senderId: this.keycloakService.userId,
                receiverId: contact.id
            };
            this.chats().unshift(chat);
            this.searchNewContact = false;
            this.chatSelected.emit(chat);
        }
    });
}

  chatClicked(chat: ChatResponse) {
    this.chatSelected.emit(chat);
  }

  wrapMessage(lastMessage: string | undefined): string {
    if (lastMessage && lastMessage.length <= 20) {
      return lastMessage;
    }
    return lastMessage?.substring(0, 17) + '...';
  }
}