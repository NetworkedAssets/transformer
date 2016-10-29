import {Component, OnInit, Input, EventEmitter, Output} from '@angular/core';
import {Bundle} from '../../manage-bundles/bundle/shared/bundle';
import {ScheduleInfo} from '../../manage-bundles/bundle/shared/scheduleInfo';
import {DocumentationProduceService} from './shared/documentation-produce.service';
import {BundleService} from '../../manage-bundles/shared/bundle.service';
import {NotifyService} from '../../notify.service';

@Component({
  moduleId: module.id,
  selector: 'app-bundle-settings',
  templateUrl: 'bundle-settings.component.html',
  styleUrls: ['bundle-settings.component.css']
})
export class BundleSettingsComponent implements OnInit {

  public updateType: string = 'Off';

  bundle: Bundle;
  @Input() formId = -1;
  @Input() originalBundle: Bundle;
  @Output() changeNotify = new EventEmitter<number>();
  @Output() saveNotify = new EventEmitter<number>();

  constructor(private documentationProduceService: DocumentationProduceService,
              private bundleService: BundleService,
              private notifyService: NotifyService) {
  }

  ngOnInit() {
    if (this.originalBundle) {
      this.bundle = Bundle.fromObject(this.originalBundle);
      if (this.bundle.scheduleInfos.length > 0) {
        this.updateType = 'Schedule';
      } else if (this.bundle.listened) {
        this.updateType = 'Git';
      } else {
        this.updateType = 'Off';
      }
    }
  }

  setPeriodic(info: ScheduleInfo, isPeriodic: boolean) {
    info.periodic = isPeriodic;
  }

  addSchedule() {
    this.bundle.scheduleInfos.push(new ScheduleInfo());
  }

  removeSchedule(info: ScheduleInfo) {
    let index = this.bundle.scheduleInfos.indexOf(info);
    this.bundle.scheduleInfos.splice(index, 1);
  }

  onSubmit() {
    this.bundle.listened = this.updateType === 'Git';
    if (this.updateType !== 'Schedule') {
      this.bundle.scheduleInfos.splice(0);
    } else {
      for (let info of this.bundle.scheduleInfos) {
        if (info.isInvalid()) {
          this.notifyService.error('Invalid schedule data.');
          return;
        }
      }
    }

    this.bundleService.updateBundle(this.bundle).subscribe(() => {
      this.originalBundle = Bundle.fromObject(this.bundle);
      this.saveNotify.emit(this.formId);
      this.notifyService.success('Documentation update type for bundle ' + this.bundle.name + ' saved.');
      this.ngOnInit();
    });
  }

  updateNow() {
    this.documentationProduceService.produceFor(this.bundle).subscribe(() => {
      this.notifyService.success('Documentation for bundle ' + this.bundle.name + ' generated.');
    });
  }

  revert() {
    this.ngOnInit();
  }

  onChange() {
    this.changeNotify.emit(this.formId);
  }

}
