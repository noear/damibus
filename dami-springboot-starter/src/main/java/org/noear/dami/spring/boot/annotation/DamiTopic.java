package org.noear.dami.spring.boot.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 大米主题组件
 *
 * @author kamosama
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface DamiTopic  {
    String[] value();
}
