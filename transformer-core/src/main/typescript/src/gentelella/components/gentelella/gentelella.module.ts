import {NgModule} from '@angular/core';
import {FooterComponent} from '../footer/footer.component';
import {SidebarComponent} from '../sidebar/sidebar.component';
import {TopNavigationComponent} from '../top-navigation/top-navigation.component';
import {GentelellaComponent} from './gentelella.component';
import {PageContentComponent} from '../page-content/page-content.component';
import {PanelComponent} from '../panel/panel.component';
import {LoginComponent} from '../login/login.component';
import {FormsModule} from '@angular/forms';
import {appRouterProviders} from '../../../app/routes';
import {BrowserModule} from '@angular/platform-browser';


@NgModule({
  declarations: [GentelellaComponent, LoginComponent, FooterComponent,
    TopNavigationComponent, PageContentComponent, PanelComponent, SidebarComponent],
  exports: [GentelellaComponent, LoginComponent, FooterComponent,
    TopNavigationComponent, PageContentComponent, PanelComponent, SidebarComponent],
  imports: [FormsModule, appRouterProviders, BrowserModule]


})
export class GentelellaModule {
}
