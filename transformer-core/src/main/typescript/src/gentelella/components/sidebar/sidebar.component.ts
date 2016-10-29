import {Component, OnInit, ElementRef, AfterViewInit, Input, ViewChildren} from '@angular/core';
import {setContentHeight} from '../gentelella/shared/index';
import {AuthService} from '../../../app/auth.service';

@Component({
  moduleId: module.id,
  selector: 'gtl-sidebar',
  templateUrl: 'sidebar.component.html',
  styleUrls: ['sidebar.component.css']
})
export class SidebarComponent implements AfterViewInit, OnInit {
  menuSections: MenuSection[];
  @Input() small: boolean;
  @Input() siteIcon: string;
  @Input() siteTitle: string;
  @Input() routeSections: {sectionName: string, routes: [any], rolesAllowed: string[]}[] = [];
  @Input() groupIcons: {[x: string]: string} = {};
  // noinspection JSMismatchedCollectionQueryUpdate
  @ViewChildren('menu') private menuElements: ElementRef[];
  private $SIDEBAR_MENUS: any;

  constructor(private auth: AuthService) {

  }

  ngOnInit() {
    this.menuSections = this.makeMenuItems(this.routeSections, this.groupIcons);
  }

  // noinspection JSUnusedGlobalSymbols
  ngAfterViewInit() {
    let menus = this.menuElements.map(e => e.nativeElement);
    this.$SIDEBAR_MENUS = jQuery(menus);
    if (jQuery.fn.mCustomScrollbar) {
      (jQuery('.menu_fixed') as any).mCustomScrollbar({
        autoHideScrollbar: true,
        theme: 'minimal',
        mouseWheel: {preventDefault: true}
      });
    }
    this.$SIDEBAR_MENUS.find('.current-page').parents('ul.child_menu')
      .slideDown(function () {
        setContentHeight();
      });
  }

  makeMenuItems(routeSections: {sectionName: string, routes: [any], rolesAllowed: string[]}[], groupToIcon: {[x: string]: string}): MenuSection[] {
    let res: MenuSection[] = [];

    for (let section of routeSections) {
      if (this.isSectionAllowed(section)) {
        let menuSection: MenuSection = {name: section.sectionName, groups: []};
        res.push(menuSection);
        let groupsMap: {[x: string]: MenuGroup} = {};

        function makeGroup(groupName: string): MenuGroup {
          if (groupsMap[groupName]) return groupsMap[groupName];

          let group: MenuGroup = {
            name: groupName,
            icon: groupToIcon[groupName] || 'fa-bars',
            items: [],
            active: false
          };

          groupsMap[groupName] = group;
          menuSection.groups.push(group);

          return group;
        }

        for (let route of section.routes) {
          if (route.group && typeof route.group === 'string') {
            let group = makeGroup(route.group);
            group.items.push(route);
            if (this.isItemActive(route)) {
              group.active = true;
            }
          }
        }
      }

    }

    return res;
  }

  isSectionAllowed(section: any): boolean {
    console.log(section);
    if (section.rolesAllowed) {
      return section.rolesAllowed.some((role: any) => this.auth.getUserRoles().indexOf(role) >= 0);
    }
    return true;
  }

  menuGroupClick(i: number, j: number, fast = false) {
    let group = this.menuSections[i].groups[j];
    let $li = jQuery(`.menu_section[data-section-index="${i}"] li[data-group-index="${j}"]`);
    if (group.active) {
      group.active = false;
      if (!fast) {
        jQuery('ul:first', $li).slideUp(function () {
          setContentHeight();
        });
      } else {
        jQuery('ul:first', $li).hide();
        setContentHeight();
      }
    } else {
      for (let section of this.menuSections) {
        for (let x of section.groups) {
          if (x !== group) x.active = false;
        }
      }
      this.$SIDEBAR_MENUS.find('li ul').slideUp();
      group.active = true;
      jQuery('ul:first', $li).slideDown(function () {
        setContentHeight();
      });
    }
  }

  isItemActive(item: any) {
    return window.location.hash === '#/condoc/' + item.path;
  }

  deactivateAll() {
    for (let i = 0; i < this.menuSections.length; ++i) {
      let menuGroups = this.menuSections[i].groups;
      for (let j = 0; j < menuGroups.length; ++j) {
        if (menuGroups[j].active) {
          this.menuGroupClick(i, j, true);
        }
      }
    }
  }
}

interface MenuSection {
  name: string;
  groups: MenuGroup[];
}

interface MenuGroup {
  name: string;
  icon: string;
  items: any[];
  active: boolean;
}

// TODO: finish the stuff with MenuSections and all
