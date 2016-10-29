import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReplaceCharPipe} from './replace-char.pipe';
import {CapitalizePipe} from './capitalize.pipe';


@NgModule({
  imports: [CommonModule],
  declarations: [ReplaceCharPipe,CapitalizePipe],
  exports: [ReplaceCharPipe,CapitalizePipe]
})
export class PipeModule {
}
