import {NgModule} from '@angular/core';
import {UserListComponent} from './user-group-list/user-group-list.component';
import {PermissionTreeComponent} from './permission-tree/permission-tree.component';
import {StructureNodesComponent} from './permission-tree/structure-nodes/structure-nodes.component';
import {UserGroupSelectComponent, UserGroupSelectModule} from '../usergroup-select/usergroup-select.component';
import {ManagePermissionsComponent} from './manage-permissions.component';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {TooltipDirectiveModule} from '../tooltip.directive';
import {PipeModule} from '../pipe.module';
import {GentelellaModule} from '../../gentelella/components/gentelella/gentelella.module';
import {ConfirmDialogModule} from '../confirm-dialog/confirm-dialog.module';


@NgModule({
  declarations: [ManagePermissionsComponent, UserListComponent, PermissionTreeComponent, StructureNodesComponent],
  imports: [BrowserModule, FormsModule, FuelUiModule, GentelellaModule, ConfirmDialogModule , 
    TooltipDirectiveModule, UserGroupSelectModule, PipeModule],
  exports: [UserListComponent, PermissionTreeComponent, StructureNodesComponent, UserGroupSelectComponent]

})
export class PermissionManagerModule {
}
