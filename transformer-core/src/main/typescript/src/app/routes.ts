import {RouterModule} from '@angular/router';
import {ManageSourcesComponent} from './manage-sources/manage-sources.component';
import {ManagePermissionsComponent} from './manage-permissions/manage-permissions.component';
import {ManageBundlesComponent} from './manage-bundles/manage-bundles.component';
import {ManageDocumentationUpdatesComponent} from './manage-documentation-updates/manage-documentation-updates.component';
import {MainComponent} from './main/main.component';
import {CondocLoginComponent} from './login/login.component';
import {AuthGuard} from './auth-guard.service';
import {ConfirmDeactivatedGuard} from './confirm-guard.service';
import {PluginManagerComponent} from './plugin-manager/plugin-manager.component';
import {UserManagerComponent} from './user-manager/user-manager.component';

type PathMatchType = 'full' | 'prefix';

export const userRoutes = [
  {
    path: 'bundles',
    component: ManageBundlesComponent,
    name: 'Bundles',
    group: 'Manage bundles',
    canDeactivate: [ConfirmDeactivatedGuard]
  },
  {
    path: 'documentation-updates',
    component: ManageDocumentationUpdatesComponent,
    name: 'Documentation Updates',
    group: 'Manage bundles',
    canDeactivate: [ConfirmDeactivatedGuard]
  },
  {
    path: '',
    redirectTo: '/condoc/bundles',
    pathMatch: 'full' as PathMatchType
  }
];

export const adminRoutes = [
  {
    path: 'sources',
    component: ManageSourcesComponent,
    name: 'Sources',
    group: 'Configuration',
    canDeactivate: [ConfirmDeactivatedGuard]

  },
  {
    path: 'permissions',
    component: ManagePermissionsComponent,
    name: 'Permissions',
    group: 'Configuration'
  },
  {
    path: 'plugins',
    component: PluginManagerComponent,
    name: 'Plugins',
    group: 'Plugins'
  },
  {
    path: 'users',
    component: UserManagerComponent,
    name: 'Users',
    group: 'Configuration',
    canDeactivate: [ConfirmDeactivatedGuard]
  }
];

export const routes = [
  {
    path: 'condoc',
    component: MainComponent,
    name: 'Main',
    children: [...userRoutes, ...adminRoutes],
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: CondocLoginComponent,
    name: 'Login'
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full' as PathMatchType
  }
];

export const groupToIcon: {[x: string]: string} = {
  'Manage bundles': 'fa-pencil-square-o',
  'Configuration': 'fa-cogs',
  'Plugins': 'fa-th'
};

export const appRouterProviders = [
  RouterModule.forRoot(routes)
];
