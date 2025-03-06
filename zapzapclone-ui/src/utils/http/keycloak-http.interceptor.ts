import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from '../keycloak/keycloak.service';

export const keycloakHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);
  const token = keycloakService.keycloak.token;
  if (token) {
    const authrequest = req.clone({
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    })
    return next(authrequest);
  }
  return next(req);
};
