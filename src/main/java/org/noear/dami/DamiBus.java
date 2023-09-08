package org.noear.dami;

import java.util.function.Consumer;

/**
 * 大米总线（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBus {
    static DamiBus global() {
        return DamiBusImpl.global();
    }

    /**
     * 获取超时
     */
    long getTimeout();

    /**
     * 设置超时
     *
     * @param timeout 超时
     */
    void setTimeout(long timeout);

    /**
     * 发送（不需要响应）
     *
     * @param topic   主题
     * @param content 内容
     */
    void send(String topic, String content);

    /**
     * 请求并等待响应
     *
     * @param topic   主题
     * @param content 内容
     */
    String requestAndResponse(String topic, String content);

    /**
     * 请求并等待回调
     *
     * @param topic    主题
     * @param content  内容
     * @param callback 回调函数
     */
    void requestAndCallback(String topic, String content, Consumer<String> callback);

    /**
     * 响应
     *
     * @param request 请求装载
     * @param content 响应内容
     */
    void response(Payload request, String content);

    /**
     * 监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    void listen(String topic, TopicListener<Payload> listener);

    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    void listen(String topic, int index, TopicListener<Payload> listener);

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    void unlisten(String topic, TopicListener<Payload> listener);
}
