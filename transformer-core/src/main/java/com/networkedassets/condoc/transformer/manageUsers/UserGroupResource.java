package com.networkedassets.condoc.transformer.manageUsers;

import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.exceptions.UserGroupNotFoundException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.security.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.networkedassets.condoc.transformer.security.Role.DOC_EDITOR;
import static com.networkedassets.condoc.transformer.security.Role.SYS_ADMIN;

@Path("usergroups")
@RolesAllowed(SYS_ADMIN)
public class UserGroupResource extends RestResource {

    @Inject
    UserGroupManager userGroupManager;

    @GET
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getAllGroups() {
        return Response.ok(userGroupManager.getAllGroups()).build();
    }

    @GET
    @JsonView(UserGroup.Views.Full.class)
    @Path("{groupId:\\d+}")
    @RolesAllowed({SYS_ADMIN, DOC_EDITOR})
    public Response getById(@PathParam("groupId") int groupId) {
        return Response
                .ok(userGroupManager.getWithUsersById(groupId).orElseThrow(UserGroupNotFoundException::new))
                .build();
    }

    @POST
    @JsonView(UserGroup.Views.Full.class)
    public Response create(@JsonView(UserGroup.Views.Full.class) UserGroup userGroup) {
        userGroupManager.persist(userGroup);
        return Response.status(Response.Status.CREATED).entity(userGroup).build();
    }

    @PUT
    @JsonView(UserGroup.Views.Full.class)
    @Path("{groupId:\\d+}")
    public Response save(@PathParam("groupId") int groupId, @JsonView(UserGroup.Views.Full.class) UserGroup userGroup) {
        return Response.ok(userGroupManager.checkRestrictionsAndSave(groupId, userGroup)).build();
    }


    @DELETE
    @Path("{groupId:\\d+}")
    public Response remove(@PathParam("groupId") int groupId) {
        userGroupManager.removeByIdIfNotAutocreated(groupId);
        return Response.noContent().build();
    }

}
