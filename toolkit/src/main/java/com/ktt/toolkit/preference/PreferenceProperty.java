package com.ktt.toolkit.preference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luke_kao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreferenceProperty {
    String key();

    String stringValue() default "";

    int intValue() default 0;

    boolean boolValue() default false;

    float floatValue() default 0.0f;

    long longValue() default 0L;
}
