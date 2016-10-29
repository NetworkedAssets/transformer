import { Injectable } from '@angular/core';
import {AuthHttp, AuthConfig} from 'angular2-jwt';
import {RequestOptions, Http, Request, RequestOptionsArgs, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Router} from '@angular/router';
import {AuthService} from './auth.service';

@Injectable()
export class CondocHttp extends AuthHttp {

  constructor(options: AuthConfig, http: Http, private router: Router, private authService: AuthService, defOpts?: RequestOptions) {
    super(options, http, defOpts);
  }

  request(url: string|Request, options?: RequestOptionsArgs): Observable<Response> {
    return super.request(url, options).catch(resp => {
      if (resp.status === 401) {
        let state = this.router.routerState.snapshot;
        if (state.url !== '/login') {
          this.authService.redirectUrl = state.url;
          console.log('login will redirect to', state.url);
          this.router.navigate(['/login']);
        }
      }
      return Observable.throw(resp);
    });
  }
}
