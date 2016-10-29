import {Component, OnInit, ViewChild} from '@angular/core';
import {Modal} from 'fuel-ui/lib/fuel-ui';
import {SourceService} from './shared/source.service';
import {SourceFormComponent} from './source/source-form/source-form.component';
import {Source} from './source/shared/source';
import {NotifyService} from '../notify.service';
import {Observable} from 'rxjs/Rx';
import {ConfirmDialogService} from '../confirm-dialog/shared/confirm-dialog.service';
import {CanComponentDeactivate} from '../confirm-guard.service';


@Component({
  moduleId: module.id,
  selector: 'app-manage-sources',
  templateUrl: 'manage-sources.component.html',
  styleUrls: ['manage-sources.component.css']

})
export class ManageSourcesComponent implements OnInit, CanComponentDeactivate {
  sources: Array<Source> = [];
  editedSources: Array<boolean> = [];
  existsChangedSources: Array<boolean> = [];
  newChangedSource = false;
  toRemove: Source;
  removingInProgress = false;
  @ViewChild('addForm') private addForm: SourceFormComponent;
  @ViewChild('removeModal') private removeModal: Modal;


  constructor(private sourcesService: SourceService, private notifyService: NotifyService, private dialogService: ConfirmDialogService) {
  }

  // noinspection JSUnusedGlobalSymbols
  ngOnInit() {
    this.updateSources();
  }

  updateSources() {
    this.sourcesService.getSources().subscribe(x => {
      this.sources = x;
      this.editedSources = this.sources.map(() => false);
      this.existsChangedSources = this.sources.map(() => false);
    });
  }


  addSource(newSource: Source) {
    this.sourcesService.addSource(newSource).subscribe(() => {
      this.updateSources();
      this.addForm.resetForm();
      this.newChangedSource = false;
      this.notifyService.success('Source added');
    });
  }

  cancelEditing(i: number) {
    return () => {
      this.editedSources[i] = false;
      this.existsChangedSources[i] = false;
    };

  }

  edit(i: number) {
    this.editedSources[i] = true;
  }

  askRemove(source: Source) {
    this.toRemove = source;
    this.removeModal.showModal(true);
  }

  remove(source: Source) {
    this.removingInProgress = true;
    this.sourcesService.removeSource(source).subscribe(() => {
      this.updateSources();
      this.removeModal.closeModal();
      this.removingInProgress = false;
      this.notifyService.success('Source removed');
    });
  }


  save(source: Source) {
    this.sourcesService.updateSource(source).subscribe(() => {
      this.updateSources();
      this.notifyService.success('Source updated');
    });
  }

  onChange(i: number) {
    if (i < 0) {
      this.newChangedSource = true;
    } else {
      this.existsChangedSources[i] = true;
    }

  }

  isChanged(): boolean {

    return this.newChangedSource || this.existsChangedSources.some(i => i === true);
  }


  canDeactivate(): boolean | Observable<boolean>| Promise<boolean> {

    if (this.isChanged()) {

      return this.dialogService.activate()
        .then((res) => {
          return res;
        });
    }
    return true;
  }
}
