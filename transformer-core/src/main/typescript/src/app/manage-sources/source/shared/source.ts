export class Source {
  static fromObject(obj: any) {
    return new Source(
      obj.url,
      obj.pluginIdentifier,
      obj.name,
      (obj.settings || [])
        .map((x: any) => SourceSettingsField.fromObject(x))
        .sort((x: SourceSettingsField, y: SourceSettingsField) => x.id > y.id),
      obj.id);
  }

  constructor(public url: string,
              public pluginIdentifier: string,
              public name: string,
              public settings: Array<SourceSettingsField>,
              public id?: number) {
  }

  toString() {
    return `Source{url: ${this.url}; pluginIdentifier: ${this.pluginIdentifier}; name: ${this.name};\
     settings: [${this.settings.join(', ')}] }`;
  }
}

export class SourceSettingsField {
  static fromObject(obj: any) {
    return new SourceSettingsField(obj.name, obj.value, obj.type, obj.id);
  }

  constructor(public name: string,
              public value: string,
              public type: string,
              public id?: number) {
  }

  toString() {
    return `${this.name}: ${this.value}`;
  }
}

export class SourceType {
  constructor(public identifier: string,
              public requiredSettingsParameters: Array<{name: string; type: 'TEXT' | 'PASSWORD'}>,
              public iconUrl: string) {
  }
}

export class SourceStructureNode {
  static fromObject(x: any): SourceStructureNode {
    return new SourceStructureNode(
      x.sourceNodeIdentifier,
      x.children.map((c: any) => SourceStructureNode.fromObject(c)),
      x.root,
      x.leaf,
      x.nodeName
    );
  }

  constructor(public sourceNodeIdentifier: SourceNodeIdentifier,
              public children: Array<SourceStructureNode>,
              public root: boolean,
              public leaf: boolean,
              public nodeName: string) {
  }
}

export interface SourceNodeIdentifier {
  sourceIdentifier: number;
  unitIdentifier: string;
}
