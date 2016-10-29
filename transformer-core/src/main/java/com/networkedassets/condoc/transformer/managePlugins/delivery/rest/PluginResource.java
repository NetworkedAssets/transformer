package com.networkedassets.condoc.transformer.managePlugins.delivery.rest;

import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.security.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static com.networkedassets.condoc.transformer.security.Role.SYS_ADMIN;

@Path("plugins")
@RolesAllowed(SYS_ADMIN)
public class PluginResource extends RestResource {

    @Inject
    private PluginManager pluginManager;

    @GET
    @Path("source")
    public Response getAllSourcePlugins() {
        return Response.ok(pluginManager.getSourcePlugins()).build();
    }

    @GET
    @Path("converter")
    public Response getAllConverterPlugins() {
        return Response.ok(pluginManager.getConverterPlugins()).build();
    }

    @GET
    @Path("source/{pluginIdentifier}/required-settings")
    public Response getSettingsRequiredByPlugin(@PathParam("pluginIdentifier") String pluginIdentifier) {

        return Response
                .ok(pluginManager.getSourcePlugins().stream().filter(p -> p.getIdentifier().equals(pluginIdentifier))
                        .findAny().map(SourcePlugin::getRequiredSettingsParameters).orElseThrow(NotFoundException::new))
                .build();
    }


}
