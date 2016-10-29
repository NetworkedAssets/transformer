package com.networkedassets.condoc.transformer.security;

import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    @Inject
    private UserManager userManager;

    private User user;

    public SecurityContextImpl() {
    }

    public SecurityContextImpl(User user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.hasRole(role);
    }

    @Override
    public boolean isSecure() {
        return user != null;
    }

    @Override
    public String getAuthenticationScheme() {
        return "other";
    }
}