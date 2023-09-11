package org.noear.dami.spring.boot;

import org.noear.dami.Dami;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * Dami 发送者扫描器
 *
 * @author kamosama
 * @since 1.0
 */
public class DamiBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public DamiBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerDefaultFilters() {
        addIncludeFilter(new AnnotationTypeFilter(DamiTopic.class));
        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping  name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' DamiSenderInterface" + ". Bean already defined with the same name!");
            return false;
        }
    }


    @Override
    protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName) {
        beanDefinition.setLazyInit(true);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            //重新加载下不然会报错
            Class<?> beanClass = ClassUtils.forName(beanClassName, ClassUtils.getDefaultClassLoader());
            beanDefinition.setBeanClass(beanClass);
            DamiTopic damiTopic = beanClass.getAnnotation(DamiTopic.class);
            beanDefinition.setInstanceSupplier(() -> Dami.api().createSender(damiTopic.value(), beanClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("beanClass is NotFound beanClassName:[%s]", beanClassName), e);
        }
    }

}