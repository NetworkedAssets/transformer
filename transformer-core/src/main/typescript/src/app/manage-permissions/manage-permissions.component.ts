import {Component, OnInit, ViewChild} from '@angular/core';
import {PermissionTreeComponent} from './permission-tree/permission-tree.component';
import {PermissionService, SourcePermissionNode} from './shared/permission.service';

@Component({
  moduleId: module.id,
  selector: 'app-manage-permissions',
  templateUrl: 'manage-permissions.component.html',
  styleUrls: ['manage-permissions.component.css']
})
export class ManagePermissionsComponent implements OnInit {
  @ViewChild(PermissionTreeComponent) private permissionTree: PermissionTreeComponent;
  permissions: Array<SourcePermissionNode>;

  constructor(private permissionService: PermissionService) {
  }

  ngOnInit() {
    this.permissionService.getAll().subscribe(perms => {
      this.permissions = perms;
      console.log(perms);
    });
  }

  expandAll() {
    this.permissionTree.expandAll();
  }

  collapseAll() {
    this.permissionTree.collapseAll();
  }

}
