import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {setContentHeight} from '../gentelella/shared/index';
import {AuthService} from '../../../app/auth.service';
import {Router} from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'gtl-top-navigation',
  templateUrl: 'top-navigation.component.html',
  styleUrls: ['top-navigation.component.css']
})
export class TopNavigationComponent implements OnInit {
  private $SIDEBAR_MENU: JQuery;
  private $BODY: JQuery;
  username: string = '';
  @Output() toggleSmallNavEvent = new EventEmitter<void>();

  constructor(private authService: AuthService, private router: Router) {
  }

  // noinspection JSUnusedGlobalSymbols
  ngOnInit() {
    this.username = this.authService.getUsername();
    this.$SIDEBAR_MENU = jQuery('#sidebar-menu');
    this.$BODY = jQuery('body');
  }


  toggleSidebar() {
    this.toggleSmallNavEvent.emit(null);
    this.$BODY.toggleClass('nav-md nav-sm');
    setContentHeight();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
