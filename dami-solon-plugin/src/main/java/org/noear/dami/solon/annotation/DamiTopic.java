package org.noear.dami.solon.annotation;

import java.lang.annotation.*;

/**
 * 大米组件
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
     * */
    String value();
}
