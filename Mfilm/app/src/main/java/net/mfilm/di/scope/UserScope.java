package net.mfilm.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Dieu on 22/02/2017.
 */

@Scope
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserScope {
}