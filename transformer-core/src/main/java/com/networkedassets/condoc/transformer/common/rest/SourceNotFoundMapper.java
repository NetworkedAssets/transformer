package com.networkedassets.condoc.transformer.common.rest;

import com.networkedassets.condoc.transformer.common.exceptions.SourceNotFoundException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SourceNotFoundMapper implements ExceptionMapper<SourceNotFoundException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(SourceNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).entity(new ErrorJson(exception)).build();
    }
}
