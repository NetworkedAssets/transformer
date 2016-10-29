import {Component, Input} from '@angular/core';
import {Source} from './shared/source';
import {CapitalizePipe} from '../../capitalize.pipe';

@Component({
  moduleId: module.id,
  selector: 'app-source',
  templateUrl: 'source.component.html',
  styleUrls: ['source.component.css']
})
export class SourceComponent {
  @Input() source = new Source(null, null, null, []);
}
