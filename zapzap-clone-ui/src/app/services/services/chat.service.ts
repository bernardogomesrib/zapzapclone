/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { ChatResponse } from '../models/chat-response';
import { getAllChats } from '../fn/chat/get-all-chats';
import { GetAllChats$Params } from '../fn/chat/get-all-chats';
import { postChat } from '../fn/chat/post-chat';
import { PostChat$Params } from '../fn/chat/post-chat';

@Injectable({ providedIn: 'root' })
export class ChatService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getAllChats()` */
  static readonly GetAllChatsPath = '/chat';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllChats()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllChats$Response(params?: GetAllChats$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ChatResponse>>> {
    return getAllChats(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllChats$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllChats(params?: GetAllChats$Params, context?: HttpContext): Observable<Array<ChatResponse>> {
    return this.getAllChats$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ChatResponse>>): Array<ChatResponse> => r.body)
    );
  }

  /** Path part for operation `postChat()` */
  static readonly PostChatPath = '/chat';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postChat()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postChat$Response(params: PostChat$Params, context?: HttpContext): Observable<StrictHttpResponse<ChatResponse>> {
    return postChat(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `postChat$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  postChat(params: PostChat$Params, context?: HttpContext): Observable<ChatResponse> {
    return this.postChat$Response(params, context).pipe(
      map((r: StrictHttpResponse<ChatResponse>): ChatResponse => r.body)
    );
  }

}
