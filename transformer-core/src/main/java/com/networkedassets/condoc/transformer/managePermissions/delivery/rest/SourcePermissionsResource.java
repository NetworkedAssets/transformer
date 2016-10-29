package com.networkedassets.condoc.transformer.managePermissions.delivery.rest;

import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.exceptions.SourcePermissionsNodeDuplicateException;
import com.networkedassets.condoc.transformer.managePermissions.core.boundary.SourcePermissionsManager;
import com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa.SourcePermissionsNode;
import com.networkedassets.condoc.transformer.security.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static com.networkedassets.condoc.transformer.security.Role.SYS_ADMIN;

@Path("permissions")
@RolesAllowed(SYS_ADMIN)
public class SourcePermissionsResource extends RestResource {

    @Inject
    private SourcePermissionsManager sourcePermissionsManager;

    @GET
    public Response getAll() {
        return Response.ok(sourcePermissionsManager.getAllNodes()).build();
    }

    @POST
    public Response createNode(SourcePermissionsNode sourcePermissionsNode) {
        try {
            sourcePermissionsManager.persist(sourcePermissionsNode);
            return Response.status(Response.Status.CREATED)
                    .entity(sourcePermissionsNode)
                    .build();
        } catch (SourcePermissionsNodeDuplicateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
