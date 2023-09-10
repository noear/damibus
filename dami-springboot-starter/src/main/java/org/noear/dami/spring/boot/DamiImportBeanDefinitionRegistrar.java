package org.noear.dami.spring.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * Dami 发送着注册器
 *
 * @author kamosama
 * @since 1.0
 */
public class DamiImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    protected static final Log logger = LogFactory.getLog(DamiImportBeanDefinitionRegistrar.class);

    private final BeanFactory beanFactory;

    public DamiImportBeanDefinitionRegistrar(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            logger.debug("Could not determine auto-configuration package, automatic damiSender scanning disabled.");
            return;
        }

        logger.debug("Searching for damiSender annotated with @DamiTopic");

        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        if (logger.isDebugEnabled()) {
            packages.forEach(pkg -> logger.debug("Using auto-configuration base package '" + pkg + "'"));
        }

        DamiBeanDefinitionScanner scanner = new DamiBeanDefinitionScanner(registry);
        scanner.scan(packages.toArray(new String[0]));
    }
}
