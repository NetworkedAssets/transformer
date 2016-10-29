import {
  Component,
  OnInit,
  Input,
  ViewChild,
  ElementRef,
  ViewEncapsulation,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges, NgModule
} from '@angular/core';
import 'rxjs/add/operator/zip';
import 'rxjs/add/operator/last';
import {UserGroup} from '../user-manager/user-group/shared/user-group';
import {UserGroupService} from '../user-manager/shared/user-group.service';
import {CommonModule} from '@angular/common';


@Component({
  moduleId: module.id,
  selector: 'app-usergroup-select',
  templateUrl: 'usergroup-select.component.html',
  styleUrls: ['usergroup-select.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class UserGroupSelectComponent implements OnInit, OnChanges {
  groups: Array<UserGroup>;

  @Input()
  selectedGroups: Array<number>;

  @ViewChild('select') selectElem: ElementRef;

  @Output()
  private onSelect = new EventEmitter<number>();

  @Output()
  private onUnselect = new EventEmitter<number>();

  constructor(private groupService: UserGroupService) {
  }

  ngOnInit() {
    // TODO: maybe optimize?
    this.groupService.getGroups().subscribe(x => this.groups = x);
    if (!this.selectedGroups)
      this.selectedGroups = [];
  }

  ngOnChanges(changes: SimpleChanges): any {
    if (changes['selectedGroups']) {
      this.initSelect2();
    }
    return null;
  }

  initSelect2() {
    let select = jQuery(this.selectElem.nativeElement) as any;
    select.select2({
      width: '300px',
      placeholder: 'Select groups'
    });

    select.on('select2:select', (e: any) => {
      this.onSelect.emit(parseInt(e.params.data.id, 10));
    });

    select.on('select2:unselect', (e: any) => {
      this.onUnselect.emit(parseInt(e.params.data.id, 10));
    });
  }
}

@NgModule({
  imports: [CommonModule],
  declarations: [UserGroupSelectComponent],
  exports: [UserGroupSelectComponent]
})
export class UserGroupSelectModule { }
