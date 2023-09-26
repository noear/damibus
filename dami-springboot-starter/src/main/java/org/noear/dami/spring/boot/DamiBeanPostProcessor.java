package org.noear.dami.spring.boot;

import org.noear.dami.Dami;
import org.noear.dami.api.Coder;
import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.exception.DamiException;
import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * Dami 后置处理器
 *
 * @author kamosama
 * @since 1.0
 */
public class DamiBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    /**
     * 销毁时移除监听器
     */
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        DamiTopic damiTopic = bean.getClass().getAnnotation(DamiTopic.class);
        assertTopicMapping(damiTopic);

        String topicMapping = damiTopic.value()[0];

        if (bean instanceof TopicListener) {
            //是TopicListener实例则使用bus移除
            Dami.bus().unlisten(topicMapping, (TopicListener) bean);
        } else {
            //否则使用api移除
            Dami.api().unregisterListener(topicMapping, bean);
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        Class<?> proxyClass = AopProxyUtils.ultimateTargetClass(bean);
        return proxyClass.isAnnotationPresent(DamiTopic.class);
    }

    /**
     * 实例化后如果有注解则注册监听器
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       // DamiTopic damiTopic = bean.getClass().getAnnotation(DamiTopic.class);
        Class<?> proxyClass = AopProxyUtils.ultimateTargetClass(bean);
        DamiTopic damiTopic = proxyClass.getAnnotation(DamiTopic.class);
        if (damiTopic != null) {
            assertTopicMapping(damiTopic);

            String topicMapping = damiTopic.value()[0];

            if (bean instanceof TopicListener) {
                //是TopicListener实例则使用bus注册
                Dami.bus().listen(topicMapping, damiTopic.index(), (TopicListener) bean);
            } else {
                //否则使用api注册
                Dami.api().registerListener(topicMapping, damiTopic.index(), bean);
            }
        }

        if (bean instanceof Interceptor) {
            Dami.intercept((Interceptor) bean);
        }

        if (bean instanceof Coder) {
            Dami.api().setCoder((Coder) bean);
        }

        return bean;
    }

    /**
     * 断言主题是否为空
     *
     * @param anno 注解
     */
    protected void assertTopicMapping(final DamiTopic anno) {
        if (anno.value().length == 0) {
            throw new DamiException("The topic cannot be empty");
        }

        if (anno.value().length != 1) {
            throw new DamiException("There can only be one topic");
        }
    }
}
