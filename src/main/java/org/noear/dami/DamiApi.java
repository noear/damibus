package org.noear.dami;

import java.util.function.Supplier;

/**
 * @author noear
 * @since 1.0
 */
public interface DamiApi {
    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    <T> T createSender(String topicMapping, Class<T> senderClz);

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    void registerListener(String topicMapping, Object listenerObj);
}
