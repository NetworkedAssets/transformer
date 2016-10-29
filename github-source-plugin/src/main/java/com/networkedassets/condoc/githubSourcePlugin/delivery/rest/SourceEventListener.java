package com.networkedassets.condoc.githubSourcePlugin.delivery.rest;

import com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent;
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
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent.OWNER_SEPARATOR;
import static com.networkedassets.condoc.githubSourcePlugin.core.entity.GithubEvent.REPOSITORY_SEPARATOR;

@Path("event")
public class SourceEventListener {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveEvent(GithubEvent githubEvent) throws NamingException {
        if (!githubEvent.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        SourceNodeIdentifierPublisher publisher = PluginManager.get().getPublisher();
        publisher.publish(buildGenericSourceEvent(githubEvent));
        return Response.accepted(githubEvent).build();
    }

    private SourceEvent buildGenericSourceEvent(GithubEvent githubEvent) {
        return new SourceEvent(
                githubEvent.getSourceUrl(),
                githubEvent.getOwnerName() + OWNER_SEPARATOR
                        + githubEvent.getRepositoryName() + REPOSITORY_SEPARATOR
                        + githubEvent.getBranchName()
        );
    }
}

/**
 * Catches RuntimeException thrown by GithubEventDeserializer when some of the JSON properties are missing
 */
@Provider
class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    public RuntimeExceptionMapper() {
    }

    @Override
    public Response toResponse(RuntimeException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
