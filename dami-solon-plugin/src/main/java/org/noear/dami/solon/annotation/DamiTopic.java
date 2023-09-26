package org.noear.dami.solon.annotation;

import java.lang.annotation.*;

/**
 * 大米主题组件
 *
 * @author noear
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DamiTopic {
    /**
     * 主题映射
     */
    String value();

    /**
     * 订阅顺序位
     */
    int index() default 0;
}
