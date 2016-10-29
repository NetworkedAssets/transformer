import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {User} from './shared/user';
import {UserGroup} from '../user-group/shared/user-group';

@Component({
  moduleId: module.id,
  selector: 'app-user',
  templateUrl: 'user.component.html',
  styleUrls: ['user.component.css']
})
export class UserComponent implements OnInit {

  @Input() user = new User('', '');
  @Input() belongingGroups: Array<UserGroup> = [];
  @Output() editEnabled = new EventEmitter<User>();
  @Output() removed = new EventEmitter<User>();

  constructor() {
  }

  ngOnInit() {
  }

  remove() {
    this.removed.emit(this.user);
  }

  enableEdit() {
    this.editEnabled.emit(this.user);
  }

}
