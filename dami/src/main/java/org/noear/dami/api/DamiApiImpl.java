package org.noear.dami.api;

import org.noear.dami.Dami;
import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.api.impl.MethodTopicListenerRecord;
import org.noear.dami.api.impl.SenderInvocationHandler;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * @author noear
 * @since 1.0
 */
public class DamiApiImpl implements DamiApi, DamiApiConfigurator {
    static final Logger log = LoggerFactory.getLogger(DamiApiImpl.class);

    /**
     * 监听器缓存（注销时用）
     */
    private Map<Class<?>, List<MethodTopicListenerRecord>> listenerMap = new HashMap<>();
    private ReentrantLock LISTENERMAP_LOCK = new ReentrantLock();

    /**
     * 编码解器
     */
    private Coder coder = new CoderDefault();

    /**
     * 总线
     */
    private final Supplier<DamiBus> busSupplier;

    public DamiApiImpl() {
        this(() -> Dami.bus());
    }

    public DamiApiImpl(DamiBus bus) {
        this(() -> bus);
    }

    public DamiApiImpl(Supplier<DamiBus> busSupplier) {
        this.busSupplier = busSupplier;
    }


    /**
     * 设置编码器
     *
     * @param coder 编码器
     */
    @Override
    public DamiApiConfigurator coder(Coder coder) {
        if (coder != null) {
            this.coder = coder;
        }

        return this;
    }

    /**
     * 获取编码器
     */
    @Override
    public Coder coder() {
        return coder;
    }

    @Override
    public DamiBus bus() {
        return busSupplier.get();
    }

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    @Override
    public <T> T createSender(String topicMapping, Class<T> senderClz) {
        Object tmp = Proxy.newProxyInstance(senderClz.getClassLoader(), new Class[]{senderClz}, new SenderInvocationHandler(this, senderClz, topicMapping));

        if (log.isDebugEnabled()) {
            log.debug("This sender created successfully(@{}.*): {}", topicMapping, senderClz.getName());
        }

        return (T) tmp;
    }

    /**
     * 注册监听者实例（一个监听类，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param listenerObj  监听器实现类
     */
    @Override
    public void registerListener(String topicMapping, int index, Object listenerObj) {
        Class<?> listenerClz = listenerObj.getClass();

        LISTENERMAP_LOCK.lock();
        try {
            //防止重复注册
            if (listenerMap.containsKey(listenerClz)) {
                throw new DamiException("This listener is registered: " + listenerClz.getName());
            }

            //开始注册
            List<MethodTopicListenerRecord> listenerRecords = new ArrayList<>();

            for (Method m1 : findMethods(listenerClz)) {
                //不能是 Object 申明的方法
                if(m1.getDeclaringClass() != Object.class) {
                    String topic = getMethodTopic(topicMapping, m1.getName());
                    MethodTopicListener listener = new MethodTopicListener(this, listenerObj, m1);

                    listenerRecords.add(new MethodTopicListenerRecord(topic, listener));
                    bus().listen(topic, index, listener);
                }
            }

            //为了注销时，移掉对应的实例
            listenerMap.put(listenerClz, listenerRecords);
        } finally {
            LISTENERMAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This listener registered successfully(@{}.*): {}", topicMapping, listenerObj.getClass().getName());
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
        LISTENERMAP_LOCK.lock();
        try {
            List<MethodTopicListenerRecord> tmp = listenerMap.remove(listenerObj.getClass());

            if (tmp != null) {
                for (MethodTopicListenerRecord r1 : tmp) {
                    bus().unlisten(r1.getTopic(), r1.getListener());
                }
            }
        } finally {
            LISTENERMAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This listener unregistered successfully(@{}.*): {}", topicMapping, listenerObj.getClass().getName());
        }
    }

    /**
     * 获取方法
     */
    protected Method[] findMethods(Class<?> listenerClz) {
        //只用公有的方法（支持承断） //old::只用自己申明的方法（不支持承断）
        return listenerClz.getMethods();
    }

    /**
     * 获取方法的主题
     */
    protected String getMethodTopic(String topicMapping, String methodName) {
        return topicMapping + "." + methodName;
    }
}
