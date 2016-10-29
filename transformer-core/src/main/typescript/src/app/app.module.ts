import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {AuthGuard} from './auth-guard.service';
import {AuthService} from './auth.service';
import {NotifyService} from './notify.service';
import {DocumentationProduceService} from './manage-documentation-updates/bundle-settings/shared/documentation-produce.service';
import {BundleService} from './manage-bundles/shared/bundle.service';
import {PermissionService} from './manage-permissions/shared/permission.service';
import {appRouterProviders} from './routes';
import {SourceService} from './manage-sources/shared/source.service';
import {HttpModule, Http} from '@angular/http';
import {FormsModule} from '@angular/forms';
import {AuthHttp, AuthConfig} from 'angular2-jwt';
import {CondocHttp} from './condoc-http.service';
import {Router} from '@angular/router';
import {PluginService} from './plugin-manager/shared/plugin.service';
import {UserService} from './user-manager/shared/user-service';
import {UserGroupService} from './user-manager/shared/user-group.service';
import {ConfirmDeactivatedGuard} from './confirm-guard.service';
import {ConfirmDialogService} from './confirm-dialog/shared/confirm-dialog.service';
import {DATEPICKER_CONTROL_VALUE_ACCESSOR} from './datepicker/datepicker.component';
import {EndpointService} from './endpoint.service';
import {CondocLoginComponent} from './login/login.component';
import {MainComponent} from './main/main.component';
import {PluginManagerComponent} from './plugin-manager/plugin-manager.component';
import {PipeModule} from './pipe.module';
import {GentelellaModule} from '../gentelella/components/gentelella/gentelella.module';
import {DocumentationUpdatesModule} from './manage-documentation-updates/documentation_updates.module';
import {UserManagerModule} from './user-manager/user-manager.module';
import {BundleManagerModule} from './manage-bundles/bundle-manager.module';
import {PermissionManagerModule} from './manage-permissions/permission-manager.module';
import {SourceManagerModule} from './manage-sources/source-manager.module';
import {LocationStrategy, HashLocationStrategy} from '@angular/common';


@NgModule({
  declarations: [AppComponent, CondocLoginComponent, MainComponent, PluginManagerComponent],
  imports: [BrowserModule, HttpModule, FormsModule, UserManagerModule, BundleManagerModule,
    DocumentationUpdatesModule, PermissionManagerModule, SourceManagerModule, GentelellaModule,
    appRouterProviders, PipeModule],
  bootstrap: [AppComponent],
  providers: [SourceService, UserGroupService, PermissionService,
    BundleService, DocumentationProduceService, NotifyService, AuthService, AuthGuard, PluginService,
    ConfirmDeactivatedGuard, ConfirmDialogService, UserService, EndpointService, DATEPICKER_CONTROL_VALUE_ACCESSOR,
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    {
      provide: AuthHttp,
      useFactory: (http: Http, router: Router, authService: AuthService) => {
        return new CondocHttp(new AuthConfig({
          globalHeaders: [{'Content-Type': 'application/json'}]
        }), http, router, authService);
      },
      deps: [Http, Router, AuthService]
    }]
})
export class AppModule {
}
