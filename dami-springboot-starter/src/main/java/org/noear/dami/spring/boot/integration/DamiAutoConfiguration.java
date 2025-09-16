package org.noear.dami.spring.boot.integration;


import org.noear.dami.Dami;
import org.noear.dami.spring.boot.DamiBeanPostProcessor;
import org.noear.dami.spring.boot.DamiImportBeanDefinitionRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Dami 自动配置类
 *
 * @author kamosama
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(Dami.class)
@Import({DamiImportBeanDefinitionRegistrar.class, DamiBeanPostProcessor.class})
public class DamiAutoConfiguration {

}
