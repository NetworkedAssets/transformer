package com.networkedassets.condoc.transformer.manageUsers;

import com.networkedassets.condoc.transformer.RestResource;
import com.networkedassets.condoc.transformer.common.exceptions.UserNotFoundException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.security.RolesAllowed;
import com.networkedassets.condoc.transformer.security.SecurityFilter;
import com.networkedassets.condoc.transformer.security.Unprotected;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.networkedassets.condoc.transformer.security.Role.SYS_ADMIN;

@Path("users")
@RolesAllowed(SYS_ADMIN)
public class UserResource extends RestResource {

    @Inject
    private UserManager userManager;

    @GET
    @Path("{username}")
    public Response getByUsername(@PathParam("username") String username) {
        return Response.ok(userManager.getByUsername(username).orElseThrow(UserNotFoundException::new)).build();
    }

    @POST
    public Response create(User user) {
        userManager.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("{username}")
    public Response save(@PathParam("username") String username, User user) {
        return Response.ok(userManager.save(username, user)).build();
    }

    @GET
    public Response getList() {
        return Response.ok(userManager.getAllUsers()).build();
    }

    @DELETE
    @Path("{username}")
    public Response remove(@PathParam("username") String username) {
        userManager.removeByUsername(username);
        return Response.noContent().build();
    }

    @PUT
    @Path("signIn")
    @Consumes("application/json")
    @Produces("application/json")
    @Unprotected
    public Response signIn(User.Credentials credentials) {
        Optional<User> user = userManager.getByCredentials(credentials);
        if (user.isPresent()) {
            String serializedJwe = SecurityFilter.generateJwt(user.get());
            Map<String, Object> response = new HashMap<>();
            response.put("token", serializedJwe);
            response.put("user", user.get());
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
