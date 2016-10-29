import {Injectable} from '@angular/core';
import {Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/catch';
import {Bundle} from '../../../manage-bundles/bundle/shared/bundle';
import {AuthHttp} from 'angular2-jwt';
import {EndpointService} from '../../../endpoint.service';

@Injectable()
export class DocumentationProduceService {

  constructor(private http: AuthHttp, private endpointService: EndpointService) {
  }

  produceFor(bundle: Bundle): Observable<Response> {
    return this.http.post(this.endpointService.getEndpoint() + `produce/${bundle.id}`, {}).catch((err: any) => {
      console.log(err);
      return Observable.of<Response>();
    });
  }

}
