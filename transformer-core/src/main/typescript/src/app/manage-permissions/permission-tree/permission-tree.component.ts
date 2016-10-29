import {Component, OnInit, QueryList, ViewChildren, Input} from '@angular/core';
import {SourceService, SourceWithStructure} from '../../manage-sources/shared/source.service';
import {StructureNodesComponent} from './structure-nodes/structure-nodes.component';
import {AccordionItem} from 'fuel-ui/lib/fuel-ui';
import {SourcePermissionNode, PermissionService} from '../shared/permission.service';
import {SourceNodeIdentifier, SourceStructureNode} from '../../manage-sources/source/shared/source';

@Component({
  moduleId: module.id,
  selector: 'app-permission-tree',
  templateUrl: 'permission-tree.component.html',
  styleUrls: ['permission-tree.component.css']
})
export class PermissionTreeComponent implements OnInit {

  sources: Array<SourceWithStructure> = [];
  @Input() permissions: Array<SourcePermissionNode>;
  doneLoading = false;

  @ViewChildren(AccordionItem) private accordionItems: QueryList<AccordionItem>;
  @ViewChildren(StructureNodesComponent) private nodes: QueryList<StructureNodesComponent>;

  constructor(private sourceService: SourceService, private permissionService: PermissionService) {
  }

  ngOnInit() {
    this.sourceService.getSourcesWithStructures().subscribe(x => {
      this.sources = x;
      this.sources.forEach((s) => this.fetchGroups(s.structure));
      this.doneLoading = true;
    });
  }

  fetchGroups(source: SourceStructureNode) {
    this.permissionService.getForNode(source.sourceNodeIdentifier).subscribe(x => {
      (source.sourceNodeIdentifier as any).userGroups = x.groups.map(g => g.id);
    });
    source.children.forEach((s) => this.fetchGroups(s));
  }

  expandAll() {
    this.accordionItems.forEach(item => item.open = true);
    this.nodes.forEach(node => node.expandAll());
  }

  collapseAll() {
    this.accordionItems.forEach(item => item.open = false);
    this.nodes.forEach(node => node.collapseAll());
  }

  grant(userGroupId: number, sourceNodeIdentifier: SourceNodeIdentifier): void {
    this.permissionService.grant(userGroupId, sourceNodeIdentifier).subscribe(/* TODO: failure handling */);
  }

  revoke(userGroupId: number, sourceNodeIdentifier: SourceNodeIdentifier): void {
    this.permissionService.revoke(userGroupId, sourceNodeIdentifier).subscribe(/* TODO: failure handling */);
  }

}
