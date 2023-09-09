package org.noear.dami.api;

import org.noear.dami.Dami;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear
 * @since 2.1
 */
public class DamiApiImpl implements DamiApi {

    Coder coder = new CoderDefault();

    @Override
    public Coder getCoder() {
        return coder;
    }

    @Override
    public void setCoder(Coder coder) {
        if (coder != null) {
            this.coder = coder;
        }
    }

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    @Override
    public <T> T createSender(String topicMapping, Class<T> senderClz) {
        return (T) Proxy.newProxyInstance(DamiApi.class.getClassLoader(), new Class[]{senderClz}, new SenderInvocationHandler(topicMapping, coder));
    }

    Map<Method, MethodTopicListener> listenerMap = new HashMap<>();

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    @Override
    public void registerListener(String topicMapping, Object listenerObj) {
        Method[] methods = listenerObj.getClass().getDeclaredMethods();

        for (Method m1 : methods) {
            MethodTopicListener listener = listenerMap.get(m1);
            if (listener == null) {
                listener = new MethodTopicListener(listenerObj, m1, coder);
                listenerMap.put(m1, listener);
            }
            String topic = topicMapping + "." + m1.getName();
            Dami.objBus().listen(topic, listener);
        }
    }

    /**
     * 取消注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    @Override
    public void unregisterListener(String topicMapping, Object listenerObj) {
        Method[] methods = listenerObj.getClass().getDeclaredMethods();

        for (Method m1 : methods) {
            MethodTopicListener listener = listenerMap.get(m1);
            if (listener != null) {
                String topic = topicMapping + "." + m1.getName();
                Dami.objBus().unlisten(topic, listener);
            }
        }
    }
}
