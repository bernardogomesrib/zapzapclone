/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { Chat } from '../models/chat';
export interface Message {
  chat?: Chat;
  content?: string;
  createdAt: string;
  id?: number;
  mediaFilePath?: string;
  receiverId?: string;
  senderId?: string;
  state?: 'SENT' | 'RECEIVED' | 'SEEN';
  type?: 'TEXT' | 'IMAGE' | 'AUDIO' | 'DOCUMENT' | 'VIDEO';
  updatedAt?: string;
}
