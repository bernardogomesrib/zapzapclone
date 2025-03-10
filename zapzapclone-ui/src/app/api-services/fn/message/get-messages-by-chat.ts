/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MessageResponse } from '../../models/message-response';

export interface GetMessagesByChat$Params {
  chatId: string;
}

export function getMessagesByChat(http: HttpClient, rootUrl: string, params: GetMessagesByChat$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<MessageResponse>>> {
  const rb = new RequestBuilder(rootUrl, getMessagesByChat.PATH, 'get');
  if (params) {
    rb.query('chatId', params.chatId, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<MessageResponse>>;
    })
  );
}

getMessagesByChat.PATH = '/message/{chatId}';
