import {Injectable} from '@angular/core';
import {AuthHttp} from 'angular2-jwt';
import {Observable} from 'rxjs/Observable';
import {EndpointService} from '../../endpoint.service';

@Injectable()
export class PluginService {

  constructor(private http: AuthHttp, private endpointService: EndpointService) {
  }

  getSourcePlugins(): Observable<Array<SourcePlugin>> {
    return this.http.get(this.endpointService.getEndpoint() + 'plugins/source').map(resp => resp.json());
  }

  getConverterPlugins(): Observable<Array<ConverterPlugin>> {
    return this.http.get(this.endpointService.getEndpoint() + 'plugins/converter').map(resp => resp.json());
  }
}

export interface Plugin {
  identifier: string;
}

export interface SourcePlugin extends Plugin {

}

export interface ConverterPlugin extends Plugin {

}
