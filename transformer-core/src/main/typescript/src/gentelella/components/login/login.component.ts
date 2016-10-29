import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {LoginData} from './login-data';

@Component({
  moduleId: module.id,
  selector: 'gtl-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css']
})
export class LoginComponent implements OnInit {

  model = new LoginData('', '');

  @Output() onLogin = new EventEmitter<LoginData>();

  onSubmit() {
    this.onLogin.emit(this.model);
  }

  constructor() {
  }

  ngOnInit() {
  }


}
