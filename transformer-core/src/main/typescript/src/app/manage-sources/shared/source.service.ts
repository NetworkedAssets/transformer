import {Injectable} from '@angular/core';
import {Response, Headers} from '@angular/http';
import {SourceType, Source, SourceStructureNode} from '../source/shared/source';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/catch';
import {NotifyService} from '../../notify.service';
import {AuthHttp} from 'angular2-jwt';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class SourceService {

  constructor(private http: AuthHttp, private notifyService: NotifyService, private endpointService: EndpointService) {
  }

  getSourceTypes(): Observable<Array<SourceType>> {
    return this.http.get(this.endpointService.getEndpoint() + 'plugins/source').map(x => x.json()).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  getSources(): Observable<Array<Source>> {
    return this.http.get(this.endpointService.getEndpoint() + 'sources')
      .map(resp =>
        resp.json().map((x: any) => Source.fromObject(x)))
      .catch((err: Response) => {
        console.log(err);
        this.notifyService.error(err.json().message);
        return Observable.throw(err);
      });

  }

  addSource(newSource: Source): Observable<Response> {
    return this.http.post(this.endpointService.getEndpoint() + 'sources', newSource).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  removeSource(source: Source): Observable<Response> {
    return this.http.delete(this.endpointService.getEndpoint() + `sources/${source.id}`).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  updateSource(source: Source): Observable<Response> {
    return this.http.put(this.endpointService.getEndpoint() + `sources/${source.id}`, source).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  getStructure(source: Source): Observable<SourceStructureNode> {
    return this.http.get(this.endpointService.getEndpoint() + `sources/${source.id}/structure`, {
      headers: new Headers({'Authorization': 'Basic YWRtaW46YWRtaW4='})
    }).map(resp => SourceStructureNode.fromObject(resp.json())).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  getSourcesWithStructures(): Observable<Array<SourceWithStructure>> {
    return Observable.fromPromise((this.getSources().toPromise().then(ss =>
      Promise.all(
        ss.map(s =>
          this.getStructure(s).map(struct => {
            (s as SourceWithStructure).structure = struct;
            return <SourceWithStructure>s;
          }).toPromise()
        )
      )
    ) as any) as Promise<Array<SourceWithStructure>>);
  }
}

export interface SourceWithStructure extends Source {
  structure: SourceStructureNode;
}
