import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  ElementRef,
  Input,
  Output,
  EventEmitter,
  ViewEncapsulation,
  forwardRef,
  Directive
} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR, NG_VALIDATORS} from '@angular/forms';
import {DatePipe} from '@angular/common';

export const DATEPICKER_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => DatepickerComponent),
  multi: true
};

const noop = () => {
};

@Component({
  moduleId: module.id,
  selector: 'app-datepicker',
  templateUrl: 'datepicker.component.html',
  styleUrls: ['datepicker.component.css'],
  encapsulation: ViewEncapsulation.None
})
// TODO: finish this component
export class DatepickerComponent implements OnInit, AfterViewInit, ControlValueAccessor {
  @ViewChild('dateContainerElem') dateContainerElem: ElementRef;
  @ViewChild('dateElem') dateElem: ElementRef;
  @Input() value: string;
  @Input() inputClasses = '';
  @Output() valueChange = new EventEmitter<string>();
  currentDate: Date;

  private afterAfterViewInit = false;
  private onTouchedCallback: () => void = noop;
  private onChangeCallback: (_: any) => void = noop;

  constructor() {
    this.currentDate = new Date();
    this.valueChange.asObservable().subscribe(val => {
      this.onChangeCallback(val);
    });
  }

  ngOnInit() {
  }

  // noinspection JSUnusedGlobalSymbols
  ngAfterViewInit() {
    let container: any = jQuery(this.dateContainerElem.nativeElement);
    // let datepickerInput = jQuery(this.dateElem.nativeElement);
    let datepicker = container.datepicker({
      format: 'yyyy-mm-dd',
      startDate: '-0d',
      weekStart: 1,
      todayBtn: 'linked',
      language: 'pl',
      todayHighlight: true,
      toggleActive: true
    });
    datepicker.datepicker('update', this.value);
    console.log('datepicker created');
    datepicker.on('changeDate clearDate', () => {
      let datePipe = new DatePipe('pl');
      this.value = datePipe.transform(datepicker.datepicker('getDate'), 'yyyy-MM-dd');
      this.valueChange.emit(this.value);
    });
    datepicker.on('blur hide', () => {
      this.onTouchedCallback();
    });

    this.afterAfterViewInit = true;
  }

  setValue(val: string) {
    if (this.afterAfterViewInit) {
      let jq: any = jQuery(this.dateContainerElem.nativeElement);
      console.log('datepicker updated to', val);
      jq.datepicker('update', val);
    }
  }

  writeValue(obj: any): void {
    this.setValue(obj);
  }

  registerOnChange(fn: any): void {
    this.onChangeCallback = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouchedCallback = fn;
  }
}

function validateNotEmpty(d: DatepickerComponent) {
  return !!d.value ? null : {validateNotEmpty: {valid: false}};
}

@Directive({
  selector: 'app-datepicker[required][ngModel]',
  providers: [{provide: NG_VALIDATORS, useValue: validateNotEmpty, multi: true}]
})
export class RequiredDateValidator {
}
