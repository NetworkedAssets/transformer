// Angular-CLI build configuration
// This file lists all the node_modules files that will be used in a build
// Also see https://github.com/angular/angular-cli/wiki/3rd-party-libs

/* global require, module */

var Angular2App = require('angular-cli/lib/broccoli/angular2-app');

module.exports = function(defaults) {
  return new Angular2App(defaults, {
    vendorNpmFiles: [
      'systemjs/dist/system-polyfills.js',
      'systemjs/dist/system.src.js',
      'zone.js/dist/**/*.+(js|js.map)',
      'es6-shim/es6-shim.js',
      'reflect-metadata/**/*.+(ts|js|js.map)',
      'rxjs/**/*.+(js|js.map)',
      '@angular/**/*.+(js|js.map)',
      'gentelella/vendors/**/*',
      'fuel-ui/bundles/*',
      'dragula/dist/dragula.js',
      'ng2-dragula/ng2-dragula.js',
      'ng2-dragula/components/**/*.js',
      'select2/dist/**/*',
      'ng2-bootstrap/**/*.js',
      'moment/moment.js',
      'angular2-jwt/**/*.js',
      'ng2-select/bundles/*'
    ]
  });
};
