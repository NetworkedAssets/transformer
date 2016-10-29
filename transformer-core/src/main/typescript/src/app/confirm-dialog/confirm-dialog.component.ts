import {Component, OnInit, ViewChild} from '@angular/core';
import {Modal} from 'fuel-ui/lib/fuel-ui';
import {ConfirmDialogService} from './shared/confirm-dialog.service';


@Component({
  moduleId: module.id,
  selector: 'app-confirm-dialog',
  templateUrl: 'confirm-dialog.component.html',
  styleUrls: ['confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit {

  @ViewChild('confirmModal') private confirmModal: Modal;

  private defaults = {
    message: 'Do you want to cancel your changes?',
  };

  private cancelButton: any;
  private okButton: any;

  message: string;


  constructor(dialogService: ConfirmDialogService) {
    dialogService.activate = this.activate.bind(this);
  }

  activate(message = this.defaults.message) {
    this.message = message;
    this.confirmModal.showModal(true);
    let promise = new Promise<boolean>(resolve => {
      this.show(resolve);
    });
    return promise;
  }


  private show(resolve: (res: boolean) => any) {

    this.confirmModal.showModal(true)
    let negativeOnClick = (e: any) => resolve(false);
    let positiveOnClick = (e: any) => resolve(true);

    if (!this.cancelButton || !this.okButton) return;


    this.cancelButton.onclick = ((e: any) => {
      e.preventDefault();
      if (!negativeOnClick(e)) this.confirmModal.closeModal();
    })

    this.okButton.onclick = ((e: any) => {
      e.preventDefault();
      if (!positiveOnClick(e)) this.confirmModal.closeModal();
    });

  }

  ngOnInit(): any {

    this.cancelButton = document.getElementById('cancelButton');
    this.okButton = document.getElementById('okButton');
  }

}
