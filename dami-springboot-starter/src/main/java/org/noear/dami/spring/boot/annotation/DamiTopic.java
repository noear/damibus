package org.noear.dami.spring.boot.annotation;

import java.lang.annotation.*;

/**
 * 大米组件
 *
 * @author kamosama
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DamiTopic  {
    String value();
}
