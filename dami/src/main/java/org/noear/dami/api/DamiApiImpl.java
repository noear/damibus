package org.noear.dami.api;

import org.noear.dami.bus.DamiBus;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.0
 */
public class DamiApiImpl implements DamiApi {
    /**
     * 监听器缓存（注销时用）
     */
    private Map<Method, MethodTopicListener> listenerMap = new HashMap<>();

    /**
     * 编码解器
     */
    private Coder coder = new CoderDefault();

    /**
     * 总线
     */
    private final DamiBus bus;

    public DamiApiImpl(DamiBus bus) {
        this.bus = bus;
    }

    /**
     * 获取编码器
     */
    @Override
    public Coder getCoder() {
        return coder;
    }

    /**
     * 设置编码器
     *
     * @param coder 编码器
     */
    @Override
    public void setCoder(Coder coder) {
        if (coder != null) {
            this.coder = coder;
        }
    }

    @Override
    public DamiBus getBus() {
        return bus;
    }

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    @Override
    public <T> T createSender(String topicMapping, Class<T> senderClz) {
        return (T) Proxy.newProxyInstance(DamiApi.class.getClassLoader(), new Class[]{senderClz}, new SenderInvocationHandler(this, senderClz, topicMapping));
    }

    /**
     * 注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param listenerObj  监听器实现类
     */
    @Override
    public synchronized void registerListener(String topicMapping, int index, Object listenerObj) {
        //只用自己申明的方法（不支持承断）
        Method[] methods = listenerObj.getClass().getDeclaredMethods();

        for (Method m1 : methods) {
            MethodTopicListener listener = listenerMap.get(m1);
            if (listener == null) {
                listener = new MethodTopicListener(this, listenerObj, m1);
                listenerMap.put(m1, listener);
            }
            String topic = topicMapping + "." + m1.getName();
            bus.listen(topic, index, listener);
        }
    }

    /**
     * 取消注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    @Override
    public synchronized void unregisterListener(String topicMapping, Object listenerObj) {
        Method[] methods = listenerObj.getClass().getDeclaredMethods();

        for (Method m1 : methods) {
            MethodTopicListener listener = listenerMap.remove(m1);
            if (listener != null) {
                String topic = topicMapping + "." + m1.getName();
                bus.unlisten(topic, listener);
            }
        }
    }
}
