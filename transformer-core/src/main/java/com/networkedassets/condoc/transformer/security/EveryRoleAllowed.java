package com.networkedassets.condoc.transformer.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default state for JAX-RS end-point authorization; can be used to override @RolesAllowed annotation on whole class
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EveryRoleAllowed {
}