import {Component, Input, ViewChild, ElementRef} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';


@Component({
  moduleId: module.id,
  selector: 'gtl-panel',
  templateUrl: 'panel.component.html',
  styleUrls: ['panel.component.css']
})
export class PanelComponent {
  @Input() sizeClasses: string;
  @Input() title: string;
  @Input() closable = false;
  @Input() hidable = true;
  @Input() settings: any[] = [];
  @ViewChild('xPanel') private xPanel: ElementRef;
  @ViewChild('collapseIcon') private collapseIcon: ElementRef;
  @ViewChild('xContent') private xContent: ElementRef;


  constructor(private sanitizer: DomSanitizer) {
  }

  getSanitizedStyle() {
    return this.sanitizer.bypassSecurityTrustStyle(
      'min-width: ' +
      (2 + (this.hidable ? 24 : 0) +
      (this.settings.length !== 0 ? 23 : 0) +
      (this.closable ? 21 : 0)) + 'px;'
    );
  }

  hidePanel() {
    let $BOX_PANEL = jQuery(this.xPanel.nativeElement),
      $ICON = jQuery(this.collapseIcon.nativeElement),
      $BOX_CONTENT = jQuery(this.xContent.nativeElement);

    // fix for some div with hardcoded fix class
    if ($BOX_PANEL.attr('style')) {
      $BOX_CONTENT.slideToggle(200, function () {
        $BOX_PANEL.removeAttr('style');
      });
    } else {
      $BOX_CONTENT.slideToggle(200);
      $BOX_PANEL.css('height', 'auto');
    }

    $ICON.toggleClass('fa-chevron-up fa-chevron-down');
  }

  closePanel() {
    jQuery(this.xPanel.nativeElement).remove();
  }
}
