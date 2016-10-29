import {Injectable} from '@angular/core';

declare var PNotify: any;

@Injectable()
export class NotifyService {

  // noinspection JSMethodCanBeStatic
  error(message: string) {
    // noinspection TsLint
    new PNotify({
      title: 'Error!',
      text: message,
      type: 'error',
      styling: 'bootstrap3'
    });
  }

  // noinspection JSMethodCanBeStatic
  info(message: string) {
    // noinspection TsLint
    new PNotify({
      title: 'Hey!',
      text: message,
      type: 'info',
      styling: 'bootstrap3'
    });
  }

  // noinspection JSMethodCanBeStatic
  success(message: string) {
    // noinspection TsLint
    new PNotify({
      title: 'Success!',
      text: message,
      type: 'success',
      styling: 'bootstrap3'
    });
  }

}
