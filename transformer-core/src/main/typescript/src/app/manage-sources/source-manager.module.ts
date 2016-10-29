import {NgModule} from '@angular/core';
import {SourceFormComponent} from './source/source-form/source-form.component';
import {SourceComponent} from './source/source.component';
import {ManageSourcesComponent} from './manage-sources.component';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {TooltipDirectiveModule} from '../tooltip.directive';
import {PipeModule} from '../pipe.module';
import {GentelellaModule} from '../../gentelella/components/gentelella/gentelella.module';
import {ConfirmDialogModule} from '../confirm-dialog/confirm-dialog.module';


@NgModule({
  declarations: [ManageSourcesComponent, SourceFormComponent, SourceComponent],
  imports: [FormsModule, BrowserModule, FuelUiModule, GentelellaModule, ConfirmDialogModule , TooltipDirectiveModule, PipeModule],
  exports: [SourceFormComponent, SourceComponent]

})
export class SourceManagerModule {
}
