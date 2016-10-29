import {DatePipe} from '@angular/common';
export class ScheduleInfo {

  public weekdays: {[x: string]: boolean} = {
    'mon': false,
    'tue': false,
    'wed': false,
    'thu': false,
    'fri': false,
    'sat': false,
    'sun': false
  };
  public periodic: boolean = false;
  public periodType: string = 'DAY';
  public oneTimeDate: string;
  public number: number = 1;
  public time: string;
  public eventType: string;

  static fromObject(obj: any) {
    console.log(obj);
    let info = new ScheduleInfo();
    for (let i in obj.weekdays)
      info.weekdays[i] = obj.weekdays[i];

    info.periodType = obj.periodType;
    info.periodic = obj.periodic;
    info.setEventType();
    info.oneTimeDate = obj.oneTimeDate;
    info.number = obj.number;
    info.time = obj.time;

    return info;
  }

  setEventType() {
    this.eventType = this.periodic ? 'periodic' : 'oneTime';
  }

  constructor() {
    let datePipe = new DatePipe('pl');

    let date = new Date();
    this.oneTimeDate = datePipe.transform(date, 'yyyy-MM-dd');
    this.time = datePipe.transform(date, 'HH:mm');
    this.setEventType();
  }

  public get daysKeys() {
    return Object.keys(this.weekdays);
  }

  toString() {
    return `ScheduleInfo{periodic: ${this.periodic};
    periodType: ${this.periodType};
    oneTimeDate: ${this.oneTimeDate};
    number: ${this.number};
    weekdays: ${this.weekdays};
    time: ${this.time} }`;
  }

  isInvalid() {
    if (this.periodic && this.periodType === 'WEEK') {
      for (let day in this.weekdays) {
        if (this.weekdays[day] === true) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
