import {Component, OnInit, ViewChild} from '@angular/core';
import {Modal} from 'fuel-ui/lib/fuel-ui';
import {DragulaService} from 'ng2-dragula/ng2-dragula';
import {Bundle} from './bundle/shared/bundle';
import {SourceService} from '../manage-sources/shared/source.service';
import {Source} from '../manage-sources/source/shared/source';
import {BundleService} from './shared/bundle.service';
import {BundleFormComponent} from './bundle/bundle-form/bundle-form.component';
import {NotifyService} from '../notify.service';
import {ConfirmDialogService} from '../confirm-dialog/shared/confirm-dialog.service';
import {Observable} from 'rxjs/Rx';
import {CanComponentDeactivate} from '../confirm-guard.service';

@Component({
  moduleId: module.id,
  selector: 'app-bundle-page',
  templateUrl: 'manage-bundles.component.html',
  styleUrls: ['manage-bundles.component.css'],
  viewProviders: [DragulaService]
})
export class ManageBundlesComponent implements OnInit, CanComponentDeactivate {

  static get DRAGULA_TARGET_ID():string {
    return 'sourceUnit';
  }

  sources:Array<Source> = [];
  bundles:Array<Bundle> = [];
  existChangedBundles:Array<boolean> = [];
  newChangedBundle = false;
  editedBundles:Array<boolean> = [];
  toRemove:Bundle;
  removingInProgress = false;
  @ViewChild('addForm') private addForm:BundleFormComponent;
  @ViewChild('removeModal') private removeModal: Modal;

  constructor(private dragulaService:DragulaService, private sourcesService:SourceService,
              private bundlesService:BundleService, private notifyService:NotifyService, private dialogService:ConfirmDialogService) {
    this.dragulaService.setOptions('items',
      {
        copy: true,
        accepts: function (el:Element, target:Element) {
          return target.id === ManageBundlesComponent.DRAGULA_TARGET_ID;
        },
        moves: function (el:Element, container:Element) {
          return container.id !== ManageBundlesComponent.DRAGULA_TARGET_ID;
        }
      }
    );
    this.dragulaService.dropModel.subscribe((value:any) => {
      this.onChange(parseInt(value.slice(2)[0].parentElement.id, 10));
    });
  }

  ngOnInit() {
    this.updateSources();
    this.updateBundles();
  }

  updateSources() {
    this.sourcesService.getSources().subscribe(x => this.sources = x);
  }

  updateBundles() {
    this.bundlesService.getBundles().subscribe(x => {
      this.bundles = x;
      this.editedBundles = this.bundles.map(() => false);
      this.existChangedBundles = this.bundles.map(() => false)
    });
  }

  addBundle(newBundle:Bundle) {
    this.bundlesService.addBundle(newBundle).subscribe(() => {
      this.updateBundles();
      this.addForm.resetForm();
      this.newChangedBundle = false;
      this.notifyService.success('Bundle added');
    });
  }

  cancelEditing(i:number) {
    return () => {
      this.editedBundles[i] = false;
      this.existChangedBundles[i] = false;
    };
  }

  edit(i:number) {
    this.editedBundles[i] = true;
  }

  askRemove(bundle:Bundle) {
    this.toRemove = bundle;
    this.removeModal.showModal(true);
  }

  remove(bundle:Bundle) {
    this.removingInProgress = true;
    this.bundlesService.removeBundle(bundle).subscribe(() => {
      this.updateBundles();
      this.removeModal.closeModal();
      this.removingInProgress = false;
      this.notifyService.success('Bundle removed');
    });
  }

  save(bundle:Bundle) {
    this.bundlesService.updateBundle(bundle).subscribe(() => {
      this.updateBundles();
      this.notifyService.success('Bundle updated');
    });
  }


  onChange(i:number) {
    if (i < 0) {
      this.newChangedBundle = true;
    } else {
      this.existChangedBundles[i] = true;
    }

  }

  isChanged():boolean {

    return this.newChangedBundle || this.existChangedBundles.some(i => i === true);
  }


  canDeactivate():boolean | Observable<boolean> |Promise<boolean> {

    if (this.isChanged()) {

      return this.dialogService.activate()
        .then((res) => {
          return res;
        });
    }
    return true;
  }


}
