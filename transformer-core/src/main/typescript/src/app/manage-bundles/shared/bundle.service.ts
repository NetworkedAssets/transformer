import {Injectable} from '@angular/core';
import {Response} from '@angular/http';
import {Bundle} from '../bundle/shared/bundle';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/catch';
import {NotifyService} from '../../notify.service';
import {AuthHttp} from 'angular2-jwt';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class BundleService {

  constructor(private http: AuthHttp, private notifyService: NotifyService, private endpointService: EndpointService) {
  }

  getBundles(): Observable<Array<Bundle>> {
    return this.http.get(this.endpointService.getEndpoint() + 'bundles').map(resp =>
      resp.json().map((x: any) => Bundle.fromObject(x)))
      .catch((err: Response) => {
        console.log(err);
        this.notifyService.error(err.json().message);
        return Observable.throw(err);
      });
  }

  addBundle(newBundle: Bundle): Observable<Response> {
    return this.http.post(this.endpointService.getEndpoint() + 'bundles', newBundle).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  removeBundle(bundle: Bundle): Observable<Response> {
    return this.http.delete(this.endpointService.getEndpoint() + `bundles/${bundle.id}`, bundle).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

  updateBundle(bundle: Bundle): Observable<Response> {
    return this.http.put(this.endpointService.getEndpoint() + `bundles/${bundle.id}`, bundle).catch((err: Response) => {
      console.log(err);
      this.notifyService.error(err.json().message);
      return Observable.throw(err);
    });
  }

}
