import {Component, OnInit, Input} from '@angular/core';
import {UserGroup} from '../../user-manager/user-group/shared/user-group';
import {SourcePermissionNode} from '../shared/permission.service';
import {UserGroupService} from '../../user-manager/shared/user-group.service';

@Component({
  moduleId: module.id,
  selector: 'app-user-group-list',
  templateUrl: 'user-group-list.component.html',
  styleUrls: ['user-group-list.component.css']
})
export class UserListComponent implements OnInit {
  userGroups: Array<UserGroup>;
  @Input() permissions: Array<SourcePermissionNode>;

  constructor(private userGroupService: UserGroupService) {
  }

  ngOnInit() {
    this.userGroupService.getGroups().subscribe(ugs => this.userGroups = ugs);
  }

}
