import {Component, AfterViewInit, ViewEncapsulation, Input, ViewChild} from '@angular/core';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {setContentHeight} from './shared/index';

@Component({
  moduleId: module.id,
  selector: 'gtl-gentelella',
  templateUrl: 'gentelella.component.html',
  styleUrls: ['gentelella.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class GentelellaComponent implements AfterViewInit {
  smallNav: boolean;
  @Input() siteTitle: string;
  @Input() siteIcon: string;
  @Input() routeSections: [{sectionName: string, routes: [any]}];
  @Input() groupIcons: {[x: string]: string};
  @ViewChild(SidebarComponent) private sidebar: SidebarComponent;

  // noinspection JSUnusedGlobalSymbols
  ngAfterViewInit() {
    jQuery(window as Object).smartresize(function () {
      setContentHeight();
    });

    setContentHeight();
    gentelella_init();
  }

  toggleSmallNav() {
    this.smallNav = !this.smallNav;
    if (this.smallNav) {
      this.sidebar.deactivateAll();
    }
  }
}

declare function gentelella_init(): any;
