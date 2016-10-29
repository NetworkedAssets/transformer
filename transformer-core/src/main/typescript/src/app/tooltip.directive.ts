import {Directive, ElementRef, OnInit, OnChanges, Input, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

@Directive({
  selector: '[tooltip]',
  host: {
    '(mouseover)': 'show()',
    '(mouseout)': 'hide()',
    '(focus)': 'show()',
    '(unfocus)': 'hide()'
  }
})
export class TooltipDirective implements OnInit, OnChanges {
  @Input('text') text: string = '';
  @Input('position') position: string = 'top';
  @Input('color') color: string = 'none';
  @Input('size') size: string = 'auto';
  @Input('rounded') rounded: boolean = false;
  @Input('always') always: boolean = false;
  private _el: HTMLElement;

  constructor(el: ElementRef) {
    this._el = el.nativeElement;
  }

  ngOnInit() {
    if (this.always) {
      this._el.classList.add('hint--always');
      this.show();
    }
  }

  ngOnChanges() {
    for (let i = 0; i < this._el.classList.length; i++) {
      const currentClass = this._el.classList[i];
      if (currentClass.startsWith('hint--')) {
        this._el.classList.remove(currentClass);
      }
    }

    if (this.always) {
      this._el.classList.add('hint--always');
      this.show();
    }
  }

  show() {
    if (!this.text || this.text.length === 0) {
      if (this._el.getAttribute('data-hint')) {
        this._el.removeAttribute('data-hint');
      }
      return;
    }

    this.hide();

    this._el.setAttribute('data-hint', this.text);

    for (let i = 0; i < this._el.classList.length; i++) {
      const currentClass = this._el.classList[i];
      if (currentClass.startsWith('hint')) {
        this._el.classList.remove(currentClass);
      }
    }

    if (this.always) {
      this._el.classList.add('hint--always');
    }

    this._el.classList.add('hint--' + this.position);

    switch (this.color) {
      case 'error':
        this._el.classList.add('hint--error');
        break;
      case 'warning':
        this._el.classList.add('hint--warning');
        break;
      case 'info':
        this._el.classList.add('hint--info');
        break;
      case 'success':
        this._el.classList.add('hint--success');
        break;
      default:

    }

    switch (this.size) {
      case 'small':
        this._el.classList.add('hint--small');
        break;
      case 'medium':
        this._el.classList.add('hint--medium');
        break;
      case 'large':
        this._el.classList.add('hint--large');
        break;
      default:
    }

    if (this.rounded) {
      this._el.classList.add('hint--rounded');
    }
  }

  hide() {
    if (this.always) {
      return;
    }

    this._el.removeAttribute('data-hint');
  }
}

@NgModule({
  imports: [CommonModule],
  declarations: [TooltipDirective],
  exports: [TooltipDirective]
})
export class TooltipDirectiveModule { }
