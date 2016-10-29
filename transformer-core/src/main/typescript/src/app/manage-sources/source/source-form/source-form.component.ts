import {Component, OnInit, Output, EventEmitter, Input, ViewChild} from '@angular/core';
import {Source, SourceType, SourceSettingsField} from '../shared/source';
import {SourceService} from '../../shared/source.service';

@Component({
  moduleId: module.id,
  selector: 'app-source-form',
  templateUrl: 'source-form.component.html',
  styleUrls: ['source-form.component.css']
})
export class SourceFormComponent implements OnInit {
  private sourceTypes: Array<SourceType> = [];
  private active = true;
  private selectedType: SourceType;
  private selectedTypeIndex: number;

  @Input() formId = -1;
  @Input() originalSource: Source;
  source = new Source(null, null, null, []);
  @Input() submitText = 'Add';
  @Input() submitColor = 'success';
  @Input() cancelText = 'Clear';
  @Input() cancelAction: {(ev?: any): void};

  @Output() sourceSubmit = new EventEmitter<Source>();
  @Output() changeNotify = new EventEmitter<number>();
  @ViewChild('#sourceForm') form: any;

  constructor(private sourcesService: SourceService) {
  }

  // noinspection JSUnusedGlobalSymbols
  ngOnInit() {

    if (this.originalSource) {
      this.source = Source.fromObject(this.originalSource);
    }
    this.sourcesService.getSourceTypes().subscribe(sts => {
      this.sourceTypes = sts;
      this.selectedTypeIndex = this.sourceTypes.findIndex(st => st.identifier === this.source.pluginIdentifier);
      this.selectedType = this.sourceTypes[this.selectedTypeIndex];
    });
  }


  changeSelectedType(newTypeIndex: number) {
    this.onChange();
    this.selectedTypeIndex = newTypeIndex;
    this.selectedType = this.sourceTypes[newTypeIndex];
    this.source.pluginIdentifier = this.selectedType.identifier;
    // TODO: merge settings?
    this.source.settings = [];
    for (let req of this.selectedType.requiredSettingsParameters) {
      this.source.settings.push(new SourceSettingsField(req.name, null, req.type));
    }
    return false;
  }

  cancel(ev?: any) {
    if (this.cancelAction) {
      this.cancelAction(ev);
    } else {
      this.resetForm(ev);
    }
  }

  resetForm(ev?: any) {
    if (ev) {
      ev.preventDefault();
    }
    this.source = new Source(null, null, null, []);
    this.selectedType = null;
    this.selectedTypeIndex = null;
    this.active = false;
    setTimeout(() => this.active = true, 0);
  }

  onSubmit() {
    this.sourceSubmit.emit(this.source);
  }

  onChange() {
    this.changeNotify.emit(this.formId);
  }

}
