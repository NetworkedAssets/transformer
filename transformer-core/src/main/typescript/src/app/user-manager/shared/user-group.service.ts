import {Injectable} from '@angular/core';
import {NotifyService} from '../../notify.service';
import {AuthHttp} from 'angular2-jwt';
import {Observable} from 'rxjs/Rx';
import {UserGroup} from '../user-group/shared/user-group';
import {Response} from '@angular/http';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class UserGroupService {

  constructor(private http: AuthHttp, private notifyService: NotifyService, private endpointService: EndpointService) {
  }

  getGroups(): Observable<Array<UserGroup>> {
    return this.http.get(this.endpointService.getEndpoint() + `usergroups`).map(resp =>
      resp.json().map((x: any) => UserGroup.fromObject(x)))
      .catch((err: Response) => {
        console.log(err);
        this.notifyService.error(err.json().message);
        return Observable.throw(err);
      });
  }

  getGroupById(id: number): Observable<UserGroup> {
    return this.http.get(this.endpointService.getEndpoint() + `usergroups/${id}`)
        .map(resp => UserGroup.fromObject(resp.json()))
        .catch((err: Response) => {
          console.log(err);
          this.notifyService.error(err.json().message);
          return Observable.throw(err);
        });
  }

  createGroup(newGroup: UserGroup): Observable<Response> {
    return this.http.post(this.endpointService.getEndpoint() + `usergroups`, newGroup).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  updateGroup(group: UserGroup): Observable<Response> {
    return this.http.put(this.endpointService.getEndpoint() + `usergroups/${group.id}`, group).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  removeGroup(group: UserGroup): Observable<Response> {
    return this.http.delete(this.endpointService.getEndpoint() + `usergroups/${group.id}`).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }
}
