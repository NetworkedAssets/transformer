import {Component, OnInit, Input, EventEmitter, Output} from '@angular/core';
import {User} from '../shared/user';

@Component({
  moduleId: module.id,
  selector: 'app-user-form',
  templateUrl: 'user-form.component.html',
  styleUrls: ['user-form.component.css']
})
export class UserFormComponent implements OnInit {

  @Input() user = new User('', '');
  @Input() isNew = false;
  @Output() editCanceled = new EventEmitter<any>();
  @Output() userSubmitted = new EventEmitter<User>();
  @Output() editingStarted = new EventEmitter<any>();

  private editedUser: User;
  private active = true;

  constructor() {
  }

  ngOnInit() {
    this.editedUser = User.copyOf(this.user);
  }

  cancelEdit() {
    this.resetForm();
    this.editCanceled.emit();
  }

  onSubmit() {
    console.log(this.editedUser);
    this.userSubmitted.emit(this.editedUser);
    this.resetForm();
  }

  resetForm() {
    this.editedUser = new User('', '');
    this.active = false;
    setTimeout(() => this.active = true, 0);
  }

  onEditingStarted() {
    this.editingStarted.emit();
  }
}
