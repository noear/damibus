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
    /**
     * 仅第一个有效（用数组是为了打断 @Component::value 的关联）
     * */
    String[] value();

    /**
     * 订阅顺序位
     */
    int index() default 0;
}
