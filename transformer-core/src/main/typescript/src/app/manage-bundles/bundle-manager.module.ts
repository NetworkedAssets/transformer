import {BundleFormComponent} from './bundle/bundle-form/bundle-form.component';
import {BundleComponent} from './bundle/bundle.component';
import {SourceUnitChooserComponent} from './source-unit-chooser/source-unit-chooser.component';
import {NgModule} from '@angular/core';
import BundleItemComponent from './bundle/bundle-item/bundle-item.component';
import {ManageBundlesComponent} from './manage-bundles.component';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {TooltipDirectiveModule} from '../tooltip.directive';
import {DragulaModule} from 'ng2-dragula/ng2-dragula';
import {UserGroupSelectModule} from '../usergroup-select/usergroup-select.component';
import {PipeModule} from '../pipe.module';
import {GentelellaModule} from '../../gentelella/components/gentelella/gentelella.module';
import {ConfirmDialogModule} from '../confirm-dialog/confirm-dialog.module';


@NgModule({
  declarations: [ManageBundlesComponent, BundleFormComponent,
    BundleComponent, SourceUnitChooserComponent, BundleItemComponent],
  imports: [FormsModule, DragulaModule, BrowserModule, FuelUiModule, GentelellaModule , ConfirmDialogModule, 
    TooltipDirectiveModule, UserGroupSelectModule,PipeModule],
  exports: [BundleFormComponent,
    BundleComponent, SourceUnitChooserComponent, BundleItemComponent]


})
export class BundleManagerModule {
}
