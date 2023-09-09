package org.noear.dami.api;

import org.noear.dami.DamiApi;
import org.noear.dami.DamiBus;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author noear
 * @since 2.1
 */
public class DamiApiImpl implements DamiApi {

    /**
     * 创建发送者代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送接口类
     */
    @Override
    public <T> T createSender(String topicMapping, Class<T> senderClz) {
        return (T) Proxy.newProxyInstance(DamiApi.class.getClassLoader(), new Class[]{senderClz}, new SenderInvocationHandler(topicMapping));
    }

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听实现类
     */
    @Override
    public void registerListener(String topicMapping, Object listenerObj) {
        Method[] methods = listenerObj.getClass().getDeclaredMethods();

        for (Method m1 : methods) {
            String topic = topicMapping + "." + m1.getName();
            DamiBus.obj().listen(topic, new MethodTopicListener(listenerObj, m1));
        }
    }
}
