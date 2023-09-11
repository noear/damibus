package org.noear.dami.spring.boot;

import org.noear.dami.Dami;
import org.noear.dami.api.Coder;
import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.spring.boot.annotation.DamiTopic;
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
        String topicMapping = bean.getClass().getAnnotation(DamiTopic.class).value();

        if (bean instanceof TopicListener){
            //是TopicListener实例则使用bus移除
            Dami.bus().unlisten(topicMapping, (TopicListener) bean);
        }else {
            //否则使用api移除
            Dami.api().unregisterListener(topicMapping, bean);
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return bean.getClass().isAnnotationPresent(DamiTopic.class);
    }

    /**
     * 实例化后如果有注解则注册监听器
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        DamiTopic damiTopic = bean.getClass().getAnnotation(DamiTopic.class);

        if (damiTopic != null) {
            String topicMapping = damiTopic.value();
            if (bean instanceof TopicListener){
                //是TopicListener实例则使用bus注册
                Dami.bus().listen(topicMapping, (TopicListener) bean);
            }else {
                //否则使用api注册
                Dami.api().registerListener(damiTopic.value(), bean);
            }
        }

        if (bean instanceof Interceptor) {
            Dami.intercept((Interceptor) bean);
        }

        if (bean instanceof Coder){
            Dami.api().setCoder((Coder) bean);
        }

        return bean;
    }

}
