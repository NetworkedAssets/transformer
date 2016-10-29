package com.networkedassets.condoc.bitbucketSourcePlugin.delivery.rest;

import com.networkedassets.condoc.bitbucketSourcePlugin.core.entity.BitbucketEvent;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceEvent;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourceNodeIdentifierPublisher;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("event")
public class SourceEventListener {

    private static final char JOIN_TOKEN = '|';

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processEvent(BitbucketEvent event) throws NamingException {
        if (event.isValidProjectChangeEvent()) {
            SourceNodeIdentifierPublisher publisher = PluginManager.get().getPublisher();
            publisher.publish(new SourceEvent(
                    event.getSourceUrl(),
                    event.getProjectKey() + JOIN_TOKEN
                            + event.getRepositorySlug() + JOIN_TOKEN
                            + event.getBranchId()
            ));
            return Response.accepted(event).build();
        } else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

