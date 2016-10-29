import {Component, OnInit} from '@angular/core';
import {groupToIcon, userRoutes, adminRoutes} from '../routes';

@Component({
  moduleId: module.id,
  selector: 'app-main',
  templateUrl: 'main.component.html',
  styleUrls: ['main.component.css']
})
export class MainComponent implements OnInit {
  routeSections:[{sectionName:string, routes:any[], rolesAllowed:string[]}];
  groupIcons:{[x:string]:string};

  ngOnInit() {
    this.routeSections = [
      {sectionName: 'general', routes: userRoutes, rolesAllowed: ['DocEditor']},
      {sectionName: 'administration', routes: adminRoutes, rolesAllowed: ['SysAdmin']}
    ];
    this.groupIcons = groupToIcon;
  }
}

