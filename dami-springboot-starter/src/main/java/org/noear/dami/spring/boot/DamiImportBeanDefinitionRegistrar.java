package org.noear.dami.spring.boot;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.noear.dami.spring.boot.annotation.DamiScan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class DamiImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    protected static final Log logger = LogFactory.getLog(DamiImportBeanDefinitionRegistrar.class);

    private final Environment environment;
    private BeanFactory beanFactory;

    DamiImportBeanDefinitionRegistrar(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Set<String> packages = getPackagesToScan(metadata);
        if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
            packages = new HashSet<>(AutoConfigurationPackages.get(beanFactory));
        }
        if (packages.isEmpty()) {
            logger.debug("Could not determine auto-configuration package, automatic damiSender scanning disabled.");
            return;
        }
        DamiBeanDefinitionScanner scanner = new DamiBeanDefinitionScanner(registry);
        scanner.scan(packages.toArray(new String[0]));
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(DamiScan.class.getName()));
        Set<String> packagesToScan = new LinkedHashSet<>();
        if (attributes != null) {
            for (String basePackage : attributes.getStringArray("basePackages")) {
                String[] tokenized = StringUtils.tokenizeToStringArray(
                        this.environment.resolvePlaceholders(basePackage),
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                Collections.addAll(packagesToScan, tokenized);
            }
            for (Class<?> basePackageClass : attributes.getClassArray("basePackageClasses")) {
                packagesToScan.add(this.environment.resolvePlaceholders(ClassUtils.getPackageName(basePackageClass)));
            }
        }
        if (packagesToScan.isEmpty()) {
            String packageName = ClassUtils.getPackageName(metadata.getClassName());
            Assert.state(StringUtils.hasLength(packageName), "@DamiScan cannot be used with the default package");
            return Collections.singleton(packageName);
        }
        return packagesToScan;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
