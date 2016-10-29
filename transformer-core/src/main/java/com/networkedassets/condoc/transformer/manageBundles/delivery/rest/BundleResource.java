package com.networkedassets.condoc.transformer.manageBundles.delivery.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.exceptions.BundleNotFoundException;
import com.networkedassets.condoc.transformer.manageBundles.core.boundary.BundleManager;
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.security.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Set;

import static com.networkedassets.condoc.transformer.security.Role.DOC_EDITOR;
import static com.networkedassets.condoc.transformer.security.Role.DOC_VIEWER;

@Path("bundles")
@RolesAllowed(DOC_EDITOR)
public class BundleResource extends RestResource {

    @Inject
    private BundleManager bundleManager;


    @GET
    @RolesAllowed(DOC_VIEWER)
    @JsonView(Bundle.Views.FullButDocItems.class)
    public Response getBundles(@Context SecurityContext securityContext) {
        // todo there should be a @QueryParam to choose whether to return bundles with groups or without
        List<Bundle> bundles = bundleManager.getAllBundlesForUserGroups(getGroupsFromContext(securityContext));
        bundles.forEach(b -> b.getUserGroups().size());
        return Response.ok(bundles).build();
    }

    @GET
    @Path("{bundleId:\\d+}")
    @JsonView(Bundle.Views.FullButDocItems.class)
    public Response getBundleById(@PathParam("bundleId") int id, @Context SecurityContext securityContext) {
        Bundle bundle = bundleManager
                .getByIdIfAnyGroupHasAccess(id, getGroupsFromContext(securityContext))
                .orElseThrow(BundleNotFoundException::new);
        return Response.ok(bundle).build();
    }

    @POST
    @JsonView(Bundle.Views.FullButDocItems.class)
    public Response createBundle(@JsonView(Bundle.Views.Full.class) Bundle bundle) {
        if (bundle.getUserGroups().isEmpty()) {
            throw new BadRequestException("At least one UserGroup is required");
        }
        bundleManager.persist(bundle);
        return Response.status(Response.Status.CREATED).entity(bundle).build();
    }

    @PUT
    @Path("{bundleId:\\d+}")
    @JsonView(Bundle.Views.FullButDocItems.class)
    public Response saveBundle(@PathParam("bundleId") int bundleId,
                               @JsonView(Bundle.Views.Full.class) Bundle bundle,
                               @Context SecurityContext securityContext) {
        if (bundle.getUserGroups().isEmpty()) {
            throw new BadRequestException("At least one UserGroup is required");
        }
        return Response.ok(bundleManager.saveForUserGroups(bundleId, bundle, getGroupsFromContext(securityContext)))
                .build();
    }

    @DELETE
    @Path("{bundleId:\\d+}")
    public Response deleteBundle(@PathParam("bundleId") int bundleId,
                                 @Context SecurityContext securityContext) {
        bundleManager.removeByIdForUserGroups(bundleId, getGroupsFromContext(securityContext));
        return Response.noContent().build();
    }

    private static Set<UserGroup> getGroupsFromContext(SecurityContext securityContext) {
        return ((User) securityContext.getUserPrincipal()).getGroups();
    }

}
