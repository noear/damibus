package org.noear.dami.api;

import org.noear.dami.api.marker.Listener;
import org.noear.dami.api.marker.Sender;

/**
 * 大米接口（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiApi {
    /**
     * 获取编码器
     */
    Coder getCoder();

    /**
     * 设置编码器
     *
     * @param coder 编码器
     */
    void setCoder(Coder coder);

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    <T extends Sender> T createSender(String topicMapping, Class<T> senderClz);

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    default <T extends Listener> void registerListener(String topicMapping, T listenerObj) {
        registerListener(topicMapping, 0, listenerObj);
    }

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param listenerObj  监听器实现类
     */
    <T extends Listener> void registerListener(String topicMapping, int index, T listenerObj);

    /**
     * 取消注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    <T extends Listener> void unregisterListener(String topicMapping, T listenerObj);
}
