import {Component, OnInit, Input} from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'gtl-page-content',
  templateUrl: 'page-content.component.html',
  styleUrls: ['page-content.component.css']
})
export class PageContentComponent implements OnInit {
  @Input() title: string;
  @Input() searchBar: boolean;
  @Input() additionalColumn: boolean=false;

  constructor() {}

  ngOnInit() {
  }

}
