/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ChatResponse } from '../../models/chat-response';

export interface GetMethodName2$Params {
}

export function getMethodName2(http: HttpClient, rootUrl: string, params?: GetMethodName2$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ChatResponse>>> {
  const rb = new RequestBuilder(rootUrl, getMethodName2.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<ChatResponse>>;
    })
  );
}

getMethodName2.PATH = '/chat';
