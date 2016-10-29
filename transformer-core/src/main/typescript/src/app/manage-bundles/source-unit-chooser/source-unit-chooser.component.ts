import {Component, OnInit} from '@angular/core';
import {Source, SourceStructureNode} from '../../manage-sources/source/shared/source';
import {SourceService} from '../../manage-sources/shared/source.service';

@Component({
  moduleId: module.id,
  selector: 'source-unit-chooser',
  templateUrl: 'source-unit-chooser.component.html',
  styleUrls: ['source-unit-chooser.component.css']
})
export class SourceUnitChooserComponent implements OnInit {
  private sources:Source[];
  private structureRootNode:SourceStructureNode;
  private chosenNodes:SourceStructureNode[];
  private chosenSource:Source;
  private sourceService:SourceService;
  private availableNodes:SourceStructureNode[];
  private loading = true;
  private draggableLevel = false;

  constructor(sourceService:SourceService) {
    this.sourceService = sourceService;
    this.chosenNodes = [];
  }

  ngOnInit():void {
    this.loading = true;
    this.sourceService.getSources().subscribe(ss => {
      this.sources = ss;
      this.loading = false;
    });
  }

  setSource(source:Source):void {
    this.chosenSource = source;
    this.loading = true;
    this.sourceService.getStructure(source).subscribe(x => {
      this.loading = false;
      this.structureRootNode = x;
      this.availableNodes = this.structureRootNode.children;
      this.computeIsDraggableLevel();
    });
  }

  setNode(node:SourceStructureNode):void {
    this.chosenNodes.push(node);
    this.availableNodes = node.children;
    this.computeIsDraggableLevel();
  }

  setLevel(level:number):void {
    if (level === 0) {
      this.chosenSource = null;
      this.chosenNodes = [];
    } else {
      this.chosenNodes = this.chosenNodes.slice(0, level - 1);
    }
    if (this.chosenNodes.length) {
      this.availableNodes = this.chosenNodes[this.chosenNodes.length - 1].children;
    } else if (this.structureRootNode) {
      this.availableNodes = this.structureRootNode.children;
    } else {
      this.availableNodes = [];
    }
    this.computeIsDraggableLevel();
  }

  // if somebody has a better idea for method name - please change it
  private computeIsDraggableLevel():void {
    this.draggableLevel = this.availableNodes.length && this.availableNodes[0].leaf;
  }

}
