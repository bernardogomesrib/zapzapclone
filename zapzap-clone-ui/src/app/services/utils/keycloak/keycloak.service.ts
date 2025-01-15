import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _keycloak: Keycloak | undefined
  constructor() { }

  get keycloak(){
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:6969/',
        realm: 'zapzap',
        clientId: 'zapzap'
      });
    }
    return this._keycloak;
  }
  async init(){
    const authenticated = await this.keycloak.init({
      onLoad:'login-required'
    });
  }
  async login(){
    await this.keycloak.login();
  }
  async logout(){
    await this.keycloak.logout();
  }
  get userId(): string{
    return this.keycloak?.tokenParsed?.sub as string;
  }
  get isTokenValid(): boolean{
    return this.keycloak?.isTokenExpired() === false;
  }
  get fullName(): string{
    return this.keycloak?.tokenParsed?.["name"] as string;
  }
  async accountManagement(){
    return this.keycloak?.accountManagement();
  }
}
