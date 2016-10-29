import {Injectable} from '@angular/core';
import {Response, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SourceNodeIdentifier} from '../../manage-sources/source/shared/source';
import {UserGroup} from '../../user-manager/user-group/shared/user-group';
import {NotifyService} from '../../notify.service';
import {AuthHttp} from 'angular2-jwt';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class PermissionService {

  constructor(private http: AuthHttp, private notifyService: NotifyService, private endpointService: EndpointService) {
  }

  getAll(): Observable<Array<SourcePermissionNode>> {
    return this.http.get(this.endpointService.getEndpoint() + 'permissions').map(resp =>
      resp.json().map((x: any) => SourcePermissionNode.fromObject(x))
    ).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  getForNode(nodeId: SourceNodeIdentifier): Observable<SourcePermissionNode> {
    return this.http
      .get(this.endpointService.getEndpoint() +
        `sources/${nodeId.sourceIdentifier}/structure/${encodeURIComponent(nodeId.unitIdentifier)}/permissions`)
      .map(resp => {
        return SourcePermissionNode.fromObject(resp.json());
      }).catch(resp => {
        if (resp.status === 404) {
          return Observable.of(new SourcePermissionNode(null, nodeId, []));
        }
        this.notifyService.error(resp.json().message);
        return Observable.throw(resp);
      });
  }

  grant(groupId: number, nodeId: SourceNodeIdentifier): Observable<Response> {
    return this.http.put(
      this.endpointService.getEndpoint() +
      `sources/${nodeId.sourceIdentifier}/structure/${encodeURIComponent(nodeId.unitIdentifier)}/permissions`,
      groupId,
      {
        headers: new Headers({
          'Content-Type': 'application/json'
        })
      }
    ).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  revoke(groupId: number, nodeId: SourceNodeIdentifier): Observable<Response> {
    return this.http.delete(this.endpointService.getEndpoint() +
      `sources/${nodeId.sourceIdentifier}/structure/${encodeURIComponent(nodeId.unitIdentifier)}/permissions?groupId=${groupId}`)
      .catch((err: Response) => {
        console.log(err);
        this.notifyService.error(err.json().message);
        return Observable.throw(err);
      });
  }
}

export class SourcePermissionNode {
  static fromObject(x: any): SourcePermissionNode {
    return new SourcePermissionNode(x.id, x.identifier, x.groups);
  }

  constructor(public id: number,
              public identifier: SourceNodeIdentifier,
              public groups: Array<UserGroup>) {
  }
}
