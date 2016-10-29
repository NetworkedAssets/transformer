import {NgModule} from '@angular/core';
import {BundleSettingsComponent} from './bundle-settings/bundle-settings.component';
import {ManageDocumentationUpdatesComponent} from './manage-documentation-updates.component';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule} from '@angular/forms';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {TooltipDirectiveModule} from '../tooltip.directive';
import {DatepickerComponent, RequiredDateValidator} from '../datepicker/datepicker.component';
import {PipeModule} from '../pipe.module';
import {ButtonsModule} from 'ng2-bootstrap/ng2-bootstrap';
import {GentelellaModule} from '../../gentelella/components/gentelella/gentelella.module';
import {ConfirmDialogModule} from '../confirm-dialog/confirm-dialog.module';


@NgModule({
  declarations: [ManageDocumentationUpdatesComponent, BundleSettingsComponent, DatepickerComponent, RequiredDateValidator],
  imports: [BrowserModule, FormsModule, FuelUiModule, GentelellaModule, ConfirmDialogModule, TooltipDirectiveModule,
    PipeModule, ButtonsModule],
  exports: [ManageDocumentationUpdatesComponent, BundleSettingsComponent, DatepickerComponent, RequiredDateValidator]


})
export class DocumentationUpdatesModule {
}
