package org.noear.dami.annotation;

import java.lang.annotation.*;

/**
 * @author noear
 * @since 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value();
}