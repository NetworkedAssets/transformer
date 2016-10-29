import {UserComponent} from './user/user.component';
import {UserFormComponent} from './user/user-form/user-form.component';
import {UserGroupComponent} from './user-group/user-group.component';
import {UserGroupFormComponent} from './user-group/user-group-form/user-group-form.component';
import {UserManagerComponent} from './user-manager.component';
import {NgModule} from '@angular/core';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {TooltipDirectiveModule} from '../tooltip.directive';
import {CommonModule} from '@angular/common';
import {SelectModule} from 'ng2-select/ng2-select';
import {ButtonsModule} from 'ng2-bootstrap/ng2-bootstrap';
import {GentelellaModule} from '../../gentelella/components/gentelella/gentelella.module';
import {ConfirmDialogModule} from '../confirm-dialog/confirm-dialog.module';



@NgModule({
  declarations: [UserManagerComponent, UserComponent, UserFormComponent, UserGroupComponent, UserGroupFormComponent],
  imports: [GentelellaModule, FuelUiModule, FormsModule, BrowserModule, CommonModule, ConfirmDialogModule,
    TooltipDirectiveModule, SelectModule, ButtonsModule],
  exports: [UserManagerComponent, UserComponent, UserFormComponent, UserGroupComponent, UserGroupFormComponent]


})
export class UserManagerModule {
}
