package com.example.ui.security.utils;

import org.ilay.NavigationAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NavigationAnnotation(RoleBasedEvaluator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecuredByRole {
    String value() default "";
}