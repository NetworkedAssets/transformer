package com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public interface Plugin extends ServletContextListener {

    String getIdentifier();

    default String getIconUrl() {
        return null;
    }

    @Override
    default void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            PluginManager man = PluginManager.get();
            man.register(this);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    default void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            PluginManager man = PluginManager.get();
            man.unregister(this);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}