import {Injectable} from '@angular/core';
import {NotifyService} from '../../notify.service';
import {AuthHttp} from 'angular2-jwt';
import {Observable} from 'rxjs/Rx';
import {User} from '../user/shared/user';
import {Response} from '@angular/http';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class UserService {

  constructor(private http: AuthHttp, private notifyService: NotifyService, private endpointService: EndpointService) {
  }

  getUsers(): Observable<Array<User>> {
    return this.http.get(this.endpointService.getEndpoint() + 'users').map(resp =>
      resp.json().map((x: any) => User.fromObject(x)))
      .catch((err: Response) => {
        console.log(err);
        this.notifyService.error(err.json().message);
        return Observable.throw(err);
      });
  }

  createUser(newUser: User): Observable<Response> {
    return this.http.post(this.endpointService.getEndpoint() + 'users', newUser).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  updateUser(user: User): Observable<Response> {
    if (user.password === '') {
      delete user.password;
    }
    return this.http.put(this.endpointService.getEndpoint() + `users/${user.username}`, user).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  removeUser(user: User): Observable<Response> {
    return this.http.delete(this.endpointService.getEndpoint() + `users/${user.username}`).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

}
