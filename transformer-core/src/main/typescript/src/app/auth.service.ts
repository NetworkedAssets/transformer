import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {tokenNotExpired, JwtHelper} from 'angular2-jwt';
import {Http} from '@angular/http';
import {EndpointService} from './endpoint.service';

const TOKEN_NAME = 'id_token';

@Injectable()
export class AuthService {

  private userRoles: Array<string> = [];
  private username: string = '';
  private jwtHelper: JwtHelper = new JwtHelper();
  redirectUrl: string;


  constructor(private http: Http, private endpointService: EndpointService) {
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.put(this.endpointService.getEndpoint() + 'users/signIn',
      {username: username, password: password}).map(resp => {
      let {token, } = resp.json();
      localStorage.setItem(TOKEN_NAME, token);
      this.getUserRolesFromToken();
      this.getUsernameFromToken();
      return true;
    }).catch(resp => {
      if (resp.status === 401) {
        return Observable.of(false);
      }
      return Observable.throw(resp);
    });
  }

  getUserRoles(): Array<string> {
    return (this.userRoles.length) ? this.userRoles : this.getUserRolesFromToken();
  }

  getUsername(): string {
    return (this.username !== '') ? this.username : this.getUsernameFromToken();
  }

  logout() {
    localStorage.removeItem(TOKEN_NAME);
  }

  isLoggedIn() {
    return tokenNotExpired();
  }

  private getUserRolesFromToken(): Array<string> {
    let token: any = this.jwtHelper.decodeToken(localStorage.getItem(TOKEN_NAME));
    this.userRoles = token.payload.roles;
    return token.payload.roles;

  }

  private getUsernameFromToken(): string {
    let token: any = this.jwtHelper.decodeToken(localStorage.getItem(TOKEN_NAME));
    this.username = token.payload.username;
    return token.payload.username;
  }

}
