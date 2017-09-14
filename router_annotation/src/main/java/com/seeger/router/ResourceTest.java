package com.seeger.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Seeger
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface ResourceTest {
    int value() default -1;
}
