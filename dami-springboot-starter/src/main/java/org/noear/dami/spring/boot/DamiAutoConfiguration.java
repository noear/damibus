package org.noear.dami.spring.boot;


import org.noear.dami.Dami;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Dami 自动配置类
 *
 * @author kamosama
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Dami.class)
@Import({DamiImportBeanDefinitionRegistrar.class, DamiBeanPostProcessor.class})
public class DamiAutoConfiguration {

}
