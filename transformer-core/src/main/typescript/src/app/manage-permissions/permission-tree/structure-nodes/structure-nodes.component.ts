import {Component, OnInit, Input, ViewChildren, QueryList} from '@angular/core';
import {SourceStructureNode, SourceNodeIdentifier} from '../../../manage-sources/source/shared/source';
import {AccordionItem} from 'fuel-ui/lib/fuel-ui';
import {PermissionService} from '../../shared/permission.service';

@Component({
  moduleId: module.id,
  selector: 'app-structure-node',
  templateUrl: 'structure-nodes.component.html',
  styleUrls: ['structure-nodes.component.css']
})
export class StructureNodesComponent implements OnInit {
  @Input() nodes: SourceStructureNode[];

  @ViewChildren(AccordionItem) private accordionItems: QueryList<AccordionItem>;
  @ViewChildren(StructureNodesComponent) private childNodes: QueryList<StructureNodesComponent>;

  constructor(private permissionService: PermissionService) {
  }

  ngOnInit() {
  }

  expandAll() {
    this.accordionItems.forEach(item => item.open = true);
    this.childNodes.forEach(node => node.expandAll());
  }

  collapseAll() {
    this.accordionItems.forEach(item => item.open = false);
    this.childNodes.forEach(node => node.collapseAll());
  }

  grant(userGroupId: number, sourceNodeIdentifier: SourceNodeIdentifier): void {
    this.permissionService.grant(userGroupId, sourceNodeIdentifier).subscribe(/* TODO: failure handling */);
  }

  revoke(userGroupId: number, sourceNodeIdentifier: SourceNodeIdentifier): void {
    this.permissionService.revoke(userGroupId, sourceNodeIdentifier).subscribe(/* TODO: failure handling */);
  }
}
