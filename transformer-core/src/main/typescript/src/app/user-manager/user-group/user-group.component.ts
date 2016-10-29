import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {UserGroup} from './shared/user-group';

@Component({
  moduleId: module.id,
  selector: 'app-user-group',
  templateUrl: 'user-group.component.html',
  styleUrls: ['user-group.component.css']
})
export class UserGroupComponent implements OnInit {

  @Input() group = new UserGroup('', false, true, [], [], undefined);
  @Output() editEnabled = new EventEmitter<UserGroup>();
  @Output() removed = new EventEmitter<UserGroup>();

  constructor() { }

  ngOnInit() {
  }

  remove() {
    this.removed.emit(this.group);
  }

  enableEdit() {
    this.editEnabled.emit(this.group);
  }

}
