import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {UserGroup} from '../shared/user-group';
import {User} from '../../user/shared/user';

@Component({
  moduleId: module.id,
  selector: 'app-user-group-form',
  templateUrl: 'user-group-form.component.html',
  styleUrls: ['user-group-form.component.css']
})
export class UserGroupFormComponent implements OnInit {

  @Input() group = new UserGroup('', false, true, [], [], undefined);
  @Input() allUsers: Array<User> = [];
  @Output() editCanceled = new EventEmitter();
  @Output() groupSubmitted = new EventEmitter<UserGroup>();
  @Output() editingStarted = new EventEmitter<any>();

  private editedGroup: UserGroup;
  private allUsernames: Array<string>;
  private selectedUsernames: Array<string>;
  private active = true;

  public sysAdmin = false;
  public docEditor = false;
  public docViewer = false;

  constructor() {
  }

  ngOnInit() {
    this.editedGroup = UserGroup.copyOf(this.group);
    this.setRolesFlagsForGroup();
    this.allUsernames = this.allUsers.map(u => u.username);
    this.selectedUsernames = this.editedGroup.users.map(u => u.username);
  }

  cancelEdit() {
    this.resetForm();
    this.editCanceled.emit();
  }

  resetForm() {
    this.editedGroup = new UserGroup('', false, true, [], [], undefined);
    this.active = false;
    setTimeout(() => this.active = true, 0);
  }

  onSubmit() {
    this.setRolesFieldByFlags();
    this.setUsersBySelectedUsernames();
    console.log(this.editedGroup);
    this.groupSubmitted.emit(this.editedGroup);
    this.resetForm();
  }

  onEditingStarted() {
    this.editingStarted.emit();
  }

  private setRolesFlagsForGroup() {
    if (this.editedGroup.roles.indexOf('SysAdmin') >= 0) {
      this.sysAdmin = true;
    }
    if (this.editedGroup.roles.indexOf('DocEditor') >= 0) {
      this.docEditor = true;
    }
    if (this.editedGroup.roles.indexOf('DocViewer') >= 0) {
      this.docViewer = true;
    }
  }

  private setRolesFieldByFlags() {
    this.editedGroup.roles = [];
    if (this.sysAdmin)
      this.editedGroup.roles.push('SysAdmin');
    if (this.docEditor)
      this.editedGroup.roles.push('DocEditor');
    if (this.docViewer)
      this.editedGroup.roles.push('DocViewer');
  }

  private onGroupsSelect(groups: Array<any>) {
    this.selectedUsernames = groups.map(g => g.id); //id is ng-select2 specific field, not the actual group id
  }

  private setUsersBySelectedUsernames() {
    this.editedGroup.users = this.allUsers.filter(u => {
      let match = false;
      this.selectedUsernames.forEach(username => {
        if (username === u.username)
          match = true;
      });
      return match;
    });
  }

}
