package com.networkedassets.condoc.transformer.common.rest;

import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TransformerExceptionMapper implements ExceptionMapper<TransformerException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(TransformerException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorJson(exception)).build();
    }
}
