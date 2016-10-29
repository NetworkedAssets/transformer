import {Component, OnInit, ViewChild} from '@angular/core';
import {Modal} from 'fuel-ui/lib/fuel-ui';
import {User} from './user/shared/user';
import {UserGroup} from './user-group/shared/user-group';
import {UserService} from './shared/user-service';
import {NotifyService} from '../notify.service';
import {UserGroupService} from './shared/user-group.service';
import {CanComponentDeactivate} from '../confirm-guard.service';
import {Observable} from 'rxjs/Rx';
import {ConfirmDialogService} from '../confirm-dialog/shared/confirm-dialog.service';


@Component({
  moduleId: module.id,
  selector: 'app-user-manager',
  templateUrl: 'user-manager.component.html',
  styleUrls: ['user-manager.component.css']
})
export class UserManagerComponent implements OnInit, CanComponentDeactivate {
  users: Array<User> = [];
  editedUsers: Array<boolean> = [];
  groups: Array<UserGroup> = [];
  editedGroups: Array<boolean> = [];
  private removingUserInProgress = false;

  private removingGroupInProgress = false;
  private userToRemove: User;
  private groupToRemove: UserGroup;
  @ViewChild('userRemoveModal') private userRemoveModal: Modal;
  @ViewChild('groupRemoveModal') private groupRemoveModal: Modal;

  //region beingEdited
  private usersBeingEdited: Array<boolean> = [];
  private groupsBeingEdited: Array<boolean> = [];
  private newUserIsBeingEdited = false;
  private newGroupIsBeingEdited = false;
  //endregion

  constructor(private userService: UserService,
              private userGroupService: UserGroupService,
              private notifyService: NotifyService,
              private dialogService: ConfirmDialogService) {
  }

  ngOnInit() {
    this.updateUsersAndGroups();
  }

  updateUsersAndGroups() {
    this.userService.getUsers().subscribe(x => this.users = x);
    this.userGroupService.getGroups().subscribe(x => this.groups = x);
  }

  createUser(user: User) {
    this.userService.createUser(user).subscribe(() => {
      this.updateUsersAndGroups();
      this.notifyService.success('User created');
    });
  }

  updateUser(user: User) {
    this.userService.updateUser(user).subscribe(() => {
      this.updateUsersAndGroups();
      this.notifyService.success('User updated');
    });
  }

  removeUser(user: User) {
    this.removingUserInProgress = true;
    this.userService.removeUser(user).subscribe(() => {
      this.updateUsersAndGroups();
      this.userRemoveModal.closeModal();
      this.removingUserInProgress = false;
      this.notifyService.success('User removed');
    });
  }

  askRemoveUser(user: User) {
    this.userToRemove = user;
    this.userRemoveModal.showModal(true);
  }

  createGroup(group: UserGroup) {
    this.userGroupService.createGroup(group).subscribe(() => {
      this.updateUsersAndGroups();
      this.notifyService.success(`Group created`);
    });
  }

  updateGroup(group: UserGroup) {
    this.userGroupService.updateGroup(group).subscribe(() => {
      this.updateUsersAndGroups();
      this.notifyService.success(`Group updated`);
    });
  }

  askRemoveGroup(group: UserGroup) {
    this.groupToRemove = group;
    this.groupRemoveModal.showModal(true);
  }

  removeGroup(group: UserGroup) {
    this.removingGroupInProgress = true;
    this.userGroupService.removeGroup(group).subscribe(() => {
      this.updateUsersAndGroups();
      this.groupRemoveModal.closeModal();
      this.removingGroupInProgress = false;
      this.notifyService.success(`Group removed`);
    });
  }

  getUsersGroups(user: User): Array<UserGroup> {
    return this.groups.filter(g => {
      let has = false;
      g.users.forEach(u => {
        if (u.username === user.username)
          has = true;
      });
      return has;
    });
  }

  canDeactivate(): boolean | Observable<boolean> |Promise<boolean> {
    if (this.isAnythingBeingEdited()) {
      return this.dialogService.activate()
        .then((res) => {
          return res;
        });
    }
    return true;
  }

  private isAnythingBeingEdited() {
    return this.newUserIsBeingEdited
      || this.newGroupIsBeingEdited
      || this.usersBeingEdited.some(u => u)
      || this.groupsBeingEdited.some(g => g);
  }
}
