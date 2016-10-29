package com.networkedassets.condoc.transformer.security;

import java.lang.annotation.*;

@Inherited
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAllowed {
    String[] value() default {};
}