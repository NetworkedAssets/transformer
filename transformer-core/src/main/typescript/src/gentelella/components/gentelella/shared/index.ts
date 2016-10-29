export function setContentHeight() {
  let $BODY = jQuery('body'),
    $LEFT_COL = jQuery('.left_col'),
    $RIGHT_COL = jQuery('.right_col'),
    $SIDEBAR_FOOTER = jQuery('.sidebar-footer'),
    $NAV_MENU = jQuery('.nav_menu'),
    $FOOTER = jQuery('footer');

  // reset height
  $RIGHT_COL.css('min-height', jQuery(window).height());
  let bodyHeight = $BODY.outerHeight(),
    footerHeight = $BODY.hasClass('footer_fixed') ? 0 : $FOOTER.height(),
    normalizeHeight = footerHeight == null ? 81 : 0,
    leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
    contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;


  // normalize content
  contentHeight -= $NAV_MENU.height() + footerHeight + 24;

  $RIGHT_COL.css('min-height', contentHeight + normalizeHeight);
}
