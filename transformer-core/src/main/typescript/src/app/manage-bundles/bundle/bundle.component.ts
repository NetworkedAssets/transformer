import {Component, Input} from '@angular/core';
import {Bundle} from './shared/bundle';

@Component({
  moduleId: module.id,
  selector: 'app-bundle',
  templateUrl: 'bundle.component.html',
  styleUrls: ['bundle.component.css']
})
export class BundleComponent {
  @Input() bundle = new Bundle(null, [], [], null);
}
