import {Component, Output, EventEmitter, Input, OnChanges} from '@angular/core';
import {SourceUnit} from '../shared/bundle';

@Component({
  moduleId: module.id,
  selector: 'app-bundle-item',
  templateUrl: 'bundle-item.component.html'
})
export default class BundleItemComponent implements OnChanges {

  @Input() item: SourceUnit;
  @Output() bundleItemRemove = new EventEmitter<SourceUnit>();
  @Output() bundleItemAdd = new EventEmitter<SourceUnit>();

  remove() {
    this.bundleItemRemove.emit(this.item);
  }


  ngOnChanges() {
    if (!this.item.id) {
      this.bundleItemAdd.emit(this.item);
    }
  }

}
