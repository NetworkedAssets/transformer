package com.networkedassets.condoc.transformer.common.rest;

import com.networkedassets.condoc.transformer.common.exceptions.SourcePermissionsNodeDuplicateException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SourcePermissionNodeDuplicateExceptionMapper implements ExceptionMapper<SourcePermissionsNodeDuplicateException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(SourcePermissionsNodeDuplicateException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorJson(exception)).build();
    }
}

