import {Component, OnInit, Input, AfterViewInit, ViewChild, ElementRef, ViewEncapsulation} from '@angular/core';
import {UserGroup} from '../../user-manager/user-group/shared/user-group';
import {SourceNodeIdentifier} from '../../manage-sources/source/shared/source';
import {SourcePermissionNode, PermissionService} from '../shared/permission.service';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/zip';
import 'rxjs/add/operator/last';
import {UserGroupService} from '../../user-manager/shared/user-group.service';

@Component({
  moduleId: module.id,
  selector: 'app-permission-select',
  templateUrl: 'permission-select.component.html',
  styleUrls: ['permission-select.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PermissionSelectComponent implements OnInit, AfterViewInit {
  groups: Array<UserGroup>;
  permissions: SourcePermissionNode;
  selectedGroups: Array<number> = [];
  private afterInitEvent = new Subject();

  @Input() sourceNode: SourceNodeIdentifier;

  @ViewChild('select') selectElem: ElementRef;

  constructor(private groupService: UserGroupService, private permissionService: PermissionService) {
  }

  ngOnInit() {
    this.groupService.getGroups().subscribe(x => this.groups = x);
    // TODO: maybe optimize?
    let permissionsForNode = this.permissionService.getForNode(this.sourceNode);
    permissionsForNode.subscribe(x => {
      this.permissions = x;
      this.selectedGroups = x.groups.map(g => g.id);
    });

    Observable.zip(permissionsForNode, this.afterInitEvent).last().subscribe(() => this.initSelect2());
  }

  // noinspection JSUnusedGlobalSymbols
  ngAfterViewInit() {
    this.afterInitEvent.next(null);
  }

  initSelect2() {
    let select = jQuery(this.selectElem.nativeElement) as any;
    select.select2({
      width: '300px',
      placeholder: 'Select groups'
    });

    select.on('select2:select', (e: any) => {
      this.permissionService.grant(parseInt(e.params.data.id, 10), this.sourceNode).subscribe(/* TODO: failure handling */);
    });

    select.on('select2:unselect', (e: any) => {
      this.permissionService.revoke(parseInt(e.params.data.id, 10), this.sourceNode).subscribe(/* TODO: failure handling */);
    });
  }
}
