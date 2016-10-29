import {Component, OnInit} from '@angular/core';
import {PluginService, SourcePlugin, ConverterPlugin} from './shared/plugin.service';

@Component({
  moduleId: module.id,
  selector: 'app-plugin-manager',
  templateUrl: 'plugin-manager.component.html',
  styleUrls: ['plugin-manager.component.css']
})
export class PluginManagerComponent implements OnInit {

  sourcePlugins: Array<SourcePlugin> = [];
  converterPlugins: Array<ConverterPlugin> = [];

  constructor(private pluginService: PluginService) {
  }

  // noinspection JSUnusedGlobalSymbols
  ngOnInit() {
    this.fetchPlugins();
  }

  fetchPlugins() {
    this.pluginService.getSourcePlugins().subscribe(x => this.sourcePlugins = x);
    this.pluginService.getConverterPlugins().subscribe(x => this.converterPlugins = x);
  }

}
