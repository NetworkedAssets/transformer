import {Injectable} from '@angular/core';

@Injectable()
export class ConfirmDialogService {
  activate: (message?: string) => Promise<boolean>;
}
