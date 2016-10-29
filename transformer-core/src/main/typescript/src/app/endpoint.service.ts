import {Injectable} from '@angular/core';

@Injectable()
export class EndpointService {

  constructor() {
  }

  // noinspection JSMethodCanBeStatic
  getEndpoint(): string {
    let port = "8080";
    if (window.location.hostname != "localhost") {
      port = window.location.port
    }
    return window.location.protocol + '//' + window.location.hostname + ':' + port + '/transformer/rest/';
  }

}
