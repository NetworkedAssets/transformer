package com.networkedassets.condoc.transformer.security;


import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networkedassets.condoc.transformer.Application;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;


@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private UserManager userManager;

    @Inject
    private UserGroupManager userGroupManager;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        User user;

        try {
            if (isProtected()) {
                user = authenticate(context);

                getAllowedRoles().ifPresent(roles -> {
                    if (!user.hasAnyOfRoles(roles)) {
                        context.abortWith(Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build());
                    }
                });

                SecurityContext securityContextImpl = new SecurityContextImpl(user);
                context.setSecurityContext(securityContextImpl);
            }
        } catch (UnauthorizedException e) {
            context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
        }
    }

    private User authenticate(ContainerRequestContext context) {
        String authorizationString = context.getHeaderString("Authorization");
        if (authorizationString == null || authorizationString.isEmpty()) {
            throw new UnauthorizedException();
        } else if (authorizationString.toLowerCase().startsWith("bearer")) {
            return authenticateJwt(authorizationString);
        } else {
            return authenticateBasic(authorizationString);
        }
    }

    private User authenticateJwt(String authorizationHeaderContent) {

        try {
            String serializedJwt = authorizationHeaderContent.substring(7);
            final JWTVerifier verifier = new JWTVerifier(Application.AUTH_SECRET);
            final Map<String, Object> claims = verifier.verify(serializedJwt);
            return userManager.getByUsername(JwtPayload.of(claims.get("payload")).getUsername()).orElseThrow(UnauthorizedException::new);
        } catch (JWTVerifyException | InvalidKeyException | SignatureException | IOException e) {
            throw new UnauthorizedException();
        } catch (NoSuchAlgorithmException e) {
            throw new TransformerException(e);
        }
    }

    private User authenticateBasic(String authorizationString) {
        if (authorizationString.startsWith("Basic ")) {
            String[] basicAuth = new String(Base64.getDecoder().decode(authorizationString.substring(6))).split(":");
            User.Credentials credentials = new User.Credentials(basicAuth[0], basicAuth[1]);
            Optional<User> user = userManager.getByCredentials(credentials);
            return user.orElseThrow(UnauthorizedException::new);
        } else {
            throw new UnauthorizedException();
        }
    }

    private Optional<Set<String>> getAllowedRoles() {
        if (isEveryRoleAllowed()) {
            //empty set is default state, which means that every role is allowed to access this resource
            return Optional.empty();
        }

        RolesAllowed annotation = getMethodRolesAnnotation();

        Set<String> set = null;

        if (annotation == null) {
            annotation = getClassRolesAnnotation();
        }

        if (annotation != null) {
            set = new HashSet<>(Arrays.asList(annotation.value()));
        }

        return Optional.ofNullable(set);
    }

    private boolean isEveryRoleAllowed() {
        return resourceInfo.getResourceMethod().getAnnotation(EveryRoleAllowed.class) != null;
    }

    private RolesAllowed getMethodRolesAnnotation() {
        return resourceInfo
                .getResourceMethod()
                .getAnnotation(RolesAllowed.class);
    }

    private RolesAllowed getClassRolesAnnotation() {
        return resourceInfo
                .getResourceClass()
                .getAnnotation(RolesAllowed.class);
    }

    private boolean isProtected() {
        return methodHasAuthenticatedAnnotation() || !(methodHasUnprotectedAnnotation() || classHasUnprotectedAnnotation());
    }

    private boolean methodHasAuthenticatedAnnotation() {
        return resourceInfo
                .getResourceMethod()
                .getAnnotation(Authenticated.class) != null;
    }

    private boolean methodHasUnprotectedAnnotation() {
        return resourceInfo
                .getResourceMethod()
                .getAnnotation(Unprotected.class) != null;
    }

    private boolean classHasUnprotectedAnnotation() {
        return resourceInfo
                .getResourceClass()
                .getAnnotation(Unprotected.class) != null;
    }

    private class UnauthorizedException extends RuntimeException {
    }

    public static class JwtPayload {
        private String username;
        private Set<String> roles;

        private JwtPayload() {
        }

        public String getUsername() {
            return username;
        }

        public JwtPayload setUsername(String username) {
            this.username = username;
            return this;
        }

        public Set<String> getRoles() {
            return roles;
        }

        public JwtPayload setRoles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public static JwtPayload of(Object map) {
            return new ObjectMapper().convertValue(map, JwtPayload.class);
        }

        public static JwtPayload of(String json) throws IOException {
            return new ObjectMapper().readValue(json, JwtPayload.class);
        }

        public static JwtPayload of(User user) {
            Set<String> roles = new HashSet<>();
            user.getGroups().forEach(group -> roles.addAll(group.getRoles()));
            return new JwtPayload().setUsername(user.getUsername()).setRoles(roles);
        }
    }

    public static String generateJwt(User user) {
        final long iat = System.currentTimeMillis() / 1000L;
        final long exp = iat + 31536000L; // 365 days todo change after fixing Confluence macro add dialog

        final JWTSigner signer = new JWTSigner(Application.AUTH_SECRET);
        final HashMap<String, Object> claims = new HashMap<>();
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("payload", SecurityFilter.JwtPayload.of(user));

        return signer.sign(claims);
    }

}