import {Component} from '@angular/core';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {LoginData} from '../../gentelella/components/login/login-data';

@Component({
  moduleId: module.id,
  selector: 'app-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css']
})
export class CondocLoginComponent {
  constructor(private authService: AuthService, private router: Router) {
  }

  login(loginData: LoginData) {
    this.authService.login(loginData.username, loginData.password).subscribe(
      (success: boolean) => {
        if (this.authService.isLoggedIn) {
          let redirect = this.authService.redirectUrl ? this.authService.redirectUrl : 'condoc/bundles';
          this.router.navigate([redirect]);
        }
        if (!success) {
          this.shakeForm();
        }
      },
      () => {
        this.shakeForm();
      }
    );
  }

  private shakeForm() {
    let l = 30;
    for (let i = 0; i < 6; i++)
      jQuery('#login-wrapper').animate({
        'margin-left': '+=' + ( l = -l ) + 'px',
        'margin-right': '-=' + l + 'px'
      }, 75);
  }
}
