package org.noear.dami.bus;

/**
 * 大米总线配置器
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBusConfigurator<C> extends DamiBus<C> {
    /**
     * 配置主题路由器
     */
    DamiBusConfigurator<C> topicRouter(TopicRouter<C> router);

    /**
     * 配置主题调度器
     */
    DamiBusConfigurator<C> topicDispatcher(TopicDispatcher<C> dispatcher);

    /**
     * 配置事件负载工厂
     */
    DamiBusConfigurator<C> messageFactory(MessageFactory<C> factory);
}
