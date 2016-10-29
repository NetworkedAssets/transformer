// TODO: remove check, rewrite in terms of $('body').on(event, selector, f), move to the document itself, not called from angular
var gentelella_inited = false;


function gentelella_init() {
  if (gentelella_inited) return;
  gentelella_inited = true;

// Tooltip
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip({
      container: 'body'
    });
  });
// /Tooltip

// Progressbar
  if ($(".progress .progress-bar")[0]) {
    $('.progress .progress-bar').progressbar();
  }
// /Progressbar

// Switchery
  $(document).ready(function () {
    if ($(".js-switch")[0]) {
      var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));
      elems.forEach(function (html) {
        var switchery = new Switchery(html, {
          color: '#26B99A'
        });
      });
    }
  });
// /Switchery

// iCheck
  $(document).ready(function () {
    if ($("input.flat")[0]) {
      $(document).ready(function () {
        $('input.flat').iCheck({
          checkboxClass: 'icheckbox_flat-green',
          radioClass: 'iradio_flat-green'
        });
      });
    }
  });
// /iCheck

// Table
  $('table input').on('ifChecked', function () {
    checkState = '';
    $(this).parent().parent().parent().addClass('selected');
    countChecked();
  });
  $('table input').on('ifUnchecked', function () {
    checkState = '';
    $(this).parent().parent().parent().removeClass('selected');
    countChecked();
  });

  var checkState = '';

  $('.bulk_action input').on('ifChecked', function () {
    checkState = '';
    $(this).parent().parent().parent().addClass('selected');
    countChecked();
  });
  $('.bulk_action input').on('ifUnchecked', function () {
    checkState = '';
    $(this).parent().parent().parent().removeClass('selected');
    countChecked();
  });
  $('.bulk_action input#check-all').on('ifChecked', function () {
    checkState = 'all';
    countChecked();
  });
  $('.bulk_action input#check-all').on('ifUnchecked', function () {
    checkState = 'none';
    countChecked();
  });

  function countChecked() {
    if (checkState === 'all') {
      $(".bulk_action input[name='table_records']").iCheck('check');
    }
    if (checkState === 'none') {
      $(".bulk_action input[name='table_records']").iCheck('uncheck');
    }

    var checkCount = $(".bulk_action input[name='table_records']:checked").length;

    if (checkCount) {
      $('.column-title').hide();
      $('.bulk-actions').show();
      $('.action-cnt').html(checkCount + ' Records Selected');
    } else {
      $('.column-title').show();
      $('.bulk-actions').hide();
    }
  }

// Accordion
  $(document).ready(function () {
    $(".expand").on("click", function () {
      $(this).next().slideToggle(200);
      $expand = $(this).find(">:first-child");

      if ($expand.text() == "+") {
        $expand.text("-");
      } else {
        $expand.text("+");
      }
    });
  });
}
