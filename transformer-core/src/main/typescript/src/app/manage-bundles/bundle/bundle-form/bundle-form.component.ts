import {Component, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {Bundle, SourceUnit} from '../shared/bundle';
import {NotifyService} from '../../../notify.service';

@Component({
  moduleId: module.id,
  selector: 'app-bundle-form',
  templateUrl: 'bundle-form.component.html',
  styleUrls: ['bundle-form.component.css']
})
export class BundleFormComponent implements OnInit {

  private active = true;

  private selectedGroupsIds: number[] = [];

  @Input() formId = -1;
  @Input() bundle = new Bundle(null, [], [], null);
  @Input() originalBundle: Bundle;
  @Input() submitText = 'Add';
  @Input() submitColor = 'success';
  @Input() cancelText = 'Clear';
  @Input() cancelAction: {(ev?: any): void};

  @Output() bundleSubmit = new EventEmitter<Bundle>();
  @Output() changeNotify = new EventEmitter<number>();

  constructor(private notifyService: NotifyService) {
  }

  ngOnInit() {
    if (this.originalBundle) {
      this.bundle = Bundle.fromObject(this.originalBundle);
      this.mapUserGroupsToNumbers();
    }
  }

  validateSourceUnit(sourceUnit: SourceUnit) {

    if (this.originalBundle && this.originalBundle.sourceUnits.some(s =>
      s.sourceNodeIdentifier.sourceIdentifier === sourceUnit.sourceNodeIdentifier.sourceIdentifier &&
      s.sourceNodeIdentifier.unitIdentifier === sourceUnit.sourceNodeIdentifier.unitIdentifier)) {
      this.bundle.removeSourceUnit(sourceUnit);
      this.notifyService.error('Source unit already exists in this bundle');
    }

    this.originalBundle = Bundle.fromObject(this.bundle);
  }

  removeSourceUnit(sourceUnit: SourceUnit) {
    this.bundle.removeSourceUnit(sourceUnit);
    this.originalBundle = Bundle.fromObject(this.bundle);
    this.onChange();
  }

  cancel(ev?: any) {
    if (this.cancelAction) {
      this.cancelAction(ev);
    } else {
      this.resetForm(ev);
    }
  }

  resetForm(ev?: any) {
    if (ev) {
      ev.preventDefault();
    }
    this.bundle = new Bundle(null, [], [], false, null);
    this.originalBundle = new Bundle(null, [], [], false, null);
    this.active = false;
    this.mapUserGroupsToNumbers();
    setTimeout(() => this.active = true, 0);
  }

  onSubmit() {
    if (!this.selectedGroupsIds.length) {
      // todo should probably be refactored into ngModel
      this.notifyService.error('At least one user group should be selected.');
    } else {
      this.bundle.userGroups = [];
      this.selectedGroupsIds.forEach(n => this.bundle.userGroups.push({id: n}));
      this.bundleSubmit.emit(this.bundle);
    }
  }

  onChange() {
    this.changeNotify.emit(this.formId);
  }


  addUserGroup(groupId: number): void {
    if (this.selectedGroupsIds.findIndex(n => n === groupId)) {
      this.selectedGroupsIds.push(groupId);
      this.onChange();
    }
  }

  removeUserGroup(groupId: number): void {
    this.selectedGroupsIds.splice(this.selectedGroupsIds.indexOf(groupId), 1);
    this.onChange();
  }

  private mapUserGroupsToNumbers() {
    // todo shouldn't be necessary after select2 refactor
    this.selectedGroupsIds = this.bundle.userGroups.map(g => g.id);
  }

}
