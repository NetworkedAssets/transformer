package com.networkedassets.condoc.transformer.manageSources.delivery.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.SourceNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.UserNotFoundException;
import com.networkedassets.condoc.transformer.managePermissions.core.boundary.SourcePermissionsManager;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.Node;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa.SourcePermissionsNode;
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.security.RolesAllowed;
import com.networkedassets.condoc.transformer.util.functional.Throwing;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static com.networkedassets.condoc.transformer.security.Role.DOC_EDITOR;
import static com.networkedassets.condoc.transformer.security.Role.SYS_ADMIN;

@Path("sources")
@RolesAllowed(SYS_ADMIN)
public class SourceResource extends RestResource {

    @Inject
    private SourceManager sourceManager;

    @Inject
    private UserManager userManager;

    @Inject
    private SourcePermissionsManager sourcePermissionsManager;

    @Inject
    private UserGroupManager userGroupManager;

    @GET
    @JsonView(Source.Views.Persisted.class)
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getSources() {
        return Response.ok(sourceManager.getAllSources()).build();
    }

    @GET
    @Path("{sourceIdentifier}")
    @JsonView(Source.Views.Full.class)
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getSourceByIdentifier(@PathParam("sourceIdentifier") Integer sourceIdentifier) {
        Source source = sourceManager
                .getWithSettingsByIdentifier(sourceIdentifier)
                .orElseThrow(SourceNotFoundException::new);
        return Response.ok(source).build();
    }

    @POST
    @JsonView(Source.Views.Full.class)
    public Response createSource(@JsonView(Source.Views.Full.class) Source source) {
        sourceManager.verifyAndPersist(source);
        return Response.status(Response.Status.CREATED).entity(source).build();
    }

    @PUT
    @Path("{sourceId}")
    @JsonView(Source.Views.Full.class)
    public Response saveSource(@JsonView(Source.Views.Full.class) Source source, @PathParam("sourceId") Integer sourceId) {
        return Response.ok(sourceManager.verifyAndSave(sourceId, source)).build();
    }

    @DELETE
    @Path("{sourceId}")
    public Response deleteSource(@PathParam("sourceId") Integer sourceId) {
        sourceManager.removeByIdentifier(sourceId);
        return Response.noContent().build();
    }

    @GET
    @Path("{sourceId}/structure")
    @JsonView(Node.Views.Tree.class)
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getStructureFromSource(@PathParam("sourceId") Integer sourceId, @Context SecurityContext securityContext) {
        SourceStructureRootNode structure = sourceManager.getStructureFromSource(sourceManager.getByIdentifierOrThrow(sourceId));
        structure = filterStructure(securityContext, structure);
        return Response.ok(structure).build();
    }

    @GET
    @Path("{sourceId}/structure/units")
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getUnitsFromSource(@PathParam("sourceId") Integer sourceId, @Context SecurityContext securityContext) {
        SourceStructureRootNode structure = sourceManager.getStructureFromSource(sourceManager.getByIdentifierOrThrow(sourceId));
        structure = filterStructure(securityContext, structure);
        return Response.ok(structure.getBelongingSourceUnits()).build();
    }

    private SourceStructureRootNode filterStructure(SecurityContext securityContext, SourceStructureRootNode structure) {
        User user = userManager.getByUsername(((User) securityContext.getUserPrincipal()).getUsername())
                .orElseThrow(UserNotFoundException::new);
        structure = sourcePermissionsManager.filterStructureByUserGroupsPermission(structure, user.getGroups());
        return structure;
    }

    @GET
    @Path("{sourceId}/structure/{unitId}/permissions")
    public Response getPermissionsForSourceNode(@PathParam("sourceId") int sourceId,
                                                @PathParam("unitId") String unitId) {
        Throwing.Function<SourcePermissionsNode, Response> makeResponse = sourcePermissionsNode ->
                Response.ok(sourcePermissionsNode).build();

        return sourcePermissionsManager.getBySourceNodeIdentifier(new SourceNodeIdentifier(sourceId, unitId))
                .map(Throwing.functionRethrowAsRuntimeException(makeResponse))
                .orElseGet(() -> Response.status(404).build());
    }

    @PUT
    @Path("{sourceId}/structure/{unitId}/permissions")
    public Response addPermissionToSourceNode(@PathParam("sourceId") Integer sourceId,
                                              @PathParam("unitId") String unitId,
                                              int groupId) {
        return userGroupManager.getById(groupId).map(userGroup -> {
            sourcePermissionsManager.grantAccess(userGroup, new SourceNodeIdentifier(sourceId, unitId));
            return Response.ok().build();
        }).orElseGet(() ->
                Response.status(Response.Status.BAD_REQUEST)
                        .entity("Group with id: " + groupId + " not found")
                        .build()
        );
    }

    @DELETE
    @Path("{sourceId}/structure/{unitId}/permissions")
    public Response removePermissionFromSourceNode(@PathParam("sourceId") Integer sourceId,
                                                   @PathParam("unitId") String unitId,
                                                   @QueryParam("groupId") int groupId) {
        return userGroupManager.getById(groupId).map(userGroup -> {
            sourcePermissionsManager.revokeAccess(userGroup, new SourceNodeIdentifier(sourceId, unitId));
            return Response.ok().build();
        }).orElseGet(() ->
                Response.status(Response.Status.BAD_REQUEST)
                        .entity("Group with id: " + groupId + " not found")
                        .build()
        );
    }

}
