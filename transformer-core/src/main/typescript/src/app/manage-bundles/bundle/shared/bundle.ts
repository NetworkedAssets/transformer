import {SourceNodeIdentifier} from '../../../manage-sources/source/shared/source';
import {ScheduleInfo} from './scheduleInfo';

export class Bundle {

  static fromObject(obj: any) {
    return new Bundle(obj.name, (obj.sourceUnits || []).map((x: any) => SourceUnit.fromObject(x)),
      (obj.scheduleInfos || []).map((x: any) => ScheduleInfo.fromObject(x)), obj.listened, obj.id, (obj.userGroups || []));
  }

  constructor(public name: string,
              public sourceUnits: Array<SourceUnit>,
              public scheduleInfos: Array<ScheduleInfo>,
              public listened: boolean,
              public id?: number,
              public userGroups?: any[]) {
    if (!this.userGroups) {
      this.userGroups = [];
    }
  }

  removeSourceUnit(sourceUnit: SourceUnit) {
    const index = this.sourceUnits.indexOf(sourceUnit);
    this.sourceUnits.splice(index, 1);
  }

  toString() {
    return `Bundle{name: ${this.name};
      sourceUnits: [${this.sourceUnits.join(', ')}];
      scheduleInfos: [${this.scheduleInfos.join(', ')}];
      listened: ${this.listened}
    }`;
  }

}

export class SourceUnit {

  static fromObject(obj: any) {
    return new SourceUnit(obj.sourceNodeIdentifier, obj.id);
  }

  constructor(public sourceNodeIdentifier: SourceNodeIdentifier,
              public id?: number) {
  }

  toString() {
    return `SourceUnit{id: ${this.id};sourceNodeIdentifier: ${this.sourceNodeIdentifier} }`;
  }

}
