import { Component, input, InputSignal, output } from '@angular/core';
import { Chat, ChatResponse, UserResponse } from '../../api-services/models';
import { last } from 'rxjs';
import { CommonModule } from '@angular/common';
import { UserService } from '../../api-services/services';

@Component({
  selector: 'app-chat-list',
  imports: [CommonModule],
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.scss',
})
export class ChatListComponent {
  chats: InputSignal<ChatResponse[]> = input<ChatResponse[]>([]);
  searchNewContact = false;
  searchContact(){
    this.searchNewContact = true;
    this.searchContactList();
  }
  contacts:Array<UserResponse> =[];
  constructor(private userService:UserService ) {}
  wrapMessage(lastMessage: string | undefined) {
    if (lastMessage && lastMessage.length > 20) {
      return lastMessage.substring(0, 20) + '...';
    } else {
      return lastMessage;
    }
  }
  chatSelected = output<ChatResponse>();
  chatClicked(chat: ChatResponse) {
    this.chatSelected.emit(chat);
  }
  selectContact(_t34: UserResponse) {
    throw new Error('Method not implemented.');
  }
  searchContactList() {
    this.userService.getAllUsersButMe(this.searchContact).subscribe({
      next: (response) => {
        this.contacts = response;
      },
    });
  }
}
