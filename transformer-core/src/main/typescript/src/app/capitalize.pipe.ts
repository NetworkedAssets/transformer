import {PipeTransform, Pipe} from '@angular/core';

// Check if the value is supported for the pipe
export function isString(txt: any): boolean {
  return typeof txt === 'string';
}

// Simple example of a Pipe
@Pipe({
  name: 'capitalize'
})
export class CapitalizePipe implements PipeTransform {
  regexp: RegExp = /([^\W_]+[^\s-]*) */g;

  static capitalizeWord(txt: string): string {
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
  }

  // noinspection JSUnusedGlobalSymbols,JSMethodCanBeStatic
  supports(txt: any): boolean {
    return isString(txt);
  }

  transform(value: string, args?: Array<any>): any {
    return (!value) ? '' :
      (!args) ?
        CapitalizePipe.capitalizeWord(value) :
        value.replace(this.regexp, CapitalizePipe.capitalizeWord);
  }
}
