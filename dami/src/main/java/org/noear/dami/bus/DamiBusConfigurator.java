package org.noear.dami.bus;

/**
 * 大米总线配置器
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBusConfigurator<C, R> extends DamiBus<C, R> {
    /**
     * 配置超时
     *
     * @param timeout 超时
     */
    DamiBusConfigurator<C, R> timeout(final long timeout);

    /**
     * 配置主题路由器
     */
    DamiBusConfigurator<C, R> topicRouter(TopicRouter<C, R> router);

    /**
     * 配置事件负载工厂
     */
    DamiBusConfigurator<C, R> payloadFactory(PayloadFactory<C, R> factory);
}
