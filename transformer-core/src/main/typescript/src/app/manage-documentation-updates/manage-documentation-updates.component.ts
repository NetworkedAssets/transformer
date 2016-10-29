import {Component, OnInit} from '@angular/core';
import {Bundle} from '../manage-bundles/bundle/shared/bundle';
import {BundleService} from '../manage-bundles/shared/bundle.service';
import {Observable} from 'rxjs';
import {ConfirmDialogService} from '../confirm-dialog/shared/confirm-dialog.service';
import {CanComponentDeactivate} from '../confirm-guard.service';

@Component({
  moduleId: module.id,
  selector: 'app-schedule-page',
  templateUrl: 'manage-documentation-updates.component.html',
  styleUrls: ['manage-documentation-updates.component.css']

})
export class ManageDocumentationUpdatesComponent implements OnInit, CanComponentDeactivate {

  bundles: Array<Bundle> = [];
  changedBundles: Array<boolean> = [];

  constructor(private bundlesService: BundleService, private dialogService: ConfirmDialogService) {
  }

  ngOnInit() {
    this.updateBundles();
  }

  updateBundles() {
    this.bundlesService.getBundles().subscribe(x => {
      this.bundles = x;
    });
  }

  onChange(i: number) {
    this.changedBundles[i] = true;
  }

  onSave(i: number) {
    this.changedBundles[i] = false;
  }

  isChanged(): boolean {
    return this.changedBundles.some(i => i === true);
  }

  canDeactivate(): boolean | Observable<boolean>| Promise<boolean> {

    if (this.isChanged()) {

      return this.dialogService.activate()
        .then((res) => {
          console.log(res);
          return res;
        });
    }
    return true;
  }
}
