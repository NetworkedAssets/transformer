import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {FuelUiModule} from 'fuel-ui/lib/fuel-ui';
import {ConfirmDialogComponent} from './confirm-dialog.component';


@NgModule({
  declarations: [ConfirmDialogComponent],
  imports: [FormsModule, BrowserModule, FuelUiModule,],
  exports: [ConfirmDialogComponent]


})
export class ConfirmDialogModule {
}
