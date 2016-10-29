// SystemJS configuration file, see links for more information
// https://github.com/systemjs/systemjs
// https://github.com/systemjs/systemjs/blob/master/docs/config-api.md

/***********************************************************************************************
 * User Configuration.
 npm install
 /** Map relative paths to URLs. */
const map: any = {
  'dragula': 'vendor/dragula/dist/dragula.js',
  'ng2-dragula': 'vendor/ng2-dragula',
  'ng2-bootstrap': 'vendor/ng2-bootstrap',
  'moment': 'vendor/moment/moment.js',
  'angular2-jwt': 'vendor/angular2-jwt/angular2-jwt.js'
};

/** User packages configuration. */
const packages: any = {
  'ng2-dragula': {format: 'cjs', defaultExtension: 'js', main: 'ng2-dragula'},
  'vendor/ng2-bootstrap': {defaultExtension: 'js'},
  // without this  error traceur is not found
  '@angular/core': {main: 'bundles/core.umd.js'},
  '@angular/common': {main: 'bundles/common.umd.js'},
  '@angular/compiler': {main: 'bundles/compiler.umd.js'},
  '@angular/forms': {main: 'bundles/forms.umd.js'},
  '@angular/http': {main: 'bundles/http.umd.js'},
  '@angular/platform-browser': {main: 'bundles/platform-browser.umd.js'},
  '@angular/platform-browser-dynamic': {main: 'bundles/platform-browser-dynamic.umd.js'},
  '@angular/router': {main: 'bundles/router.umd.js'}
};

////////////////////////////////////////////////////////////////////////////////////////////////
/***********************************************************************************************
 * Everything underneath this line is managed by the CLI.
 **********************************************************************************************/
const barrels: string[] = [
  // Angular specific barrels.
  '@angular/core',
  '@angular/common',
  '@angular/compiler',
  '@angular/forms',
  '@angular/http',
  '@angular/router',
  '@angular/platform-browser',
  '@angular/platform-browser-dynamic',

  // Thirdparty barrels.
  'rxjs',

  // App specific barrels.
  'gentelella/components/sidebar',
  'gentelella/components/top-navigation',
  'gentelella/components/footer',
  'gentelella/components/page-content',
  'gentelella/components/gentelella',
  'gentelella/components/panel',
  'gentelella/components/login',
  'app',
  'app/shared',
  'app/empty-page',
  'app/manage-sources',
  'app/manage-sources/source',
  'app/manage-permissions',
  'app/manage-permissions/user-group-list',
  'app/manage-permissions/permission-tree',
  'app/manage-permissions/permission-tree/structure-nodes',
  'app/manage-permissions/usergroup-select',
  'app/datepicker',
  'app/manage-documentation-updates/bundle-settings',
  'app/main',
  'app/login',
  'app/plugin-manager',
  'app/confirm-dialog',
  'app/user-manager',
  'app/user-manager/user',
  'app/user-manager/user-group',
  'app/user-manager/user/user-form',
  'app/user-manager/user-group/user-group-form',
  /** @cli-barrel */
];

const cliSystemConfigPackages: any = {};
barrels.forEach((barrelName: string) => {
  cliSystemConfigPackages[barrelName] = {main: 'index'};
});

/** Type declaration for ambient System. */
declare var System: any;

// Apply the CLI SystemJS configuration.
System.config({
  map: {
    '@angular': 'vendor/@angular',
    'rxjs': 'vendor/rxjs',
    'main': 'main.js'
  },
  packages: cliSystemConfigPackages
});

// Apply the user's configuration.
System.config({map, packages});
