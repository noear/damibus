/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami.lpc;

import org.noear.dami.Dami;
import org.noear.dami.lpc.impl.ServiceMethodTopicListener;
import org.noear.dami.bus.TopicListenRecord;
import org.noear.dami.lpc.impl.ConsumerInvocationHandler;
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
 * 大米本地过程调用实现
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public class DamiLpcImpl implements DamiLpc, DamiLpcConfigurator {
    static final Logger log = LoggerFactory.getLogger(DamiLpcImpl.class);

    /**
     * 监听器缓存（注销时用）
     */
    private Map<Class<?>, List<TopicListenRecord>> serviceMap = new HashMap<>();
    private ReentrantLock SERVICE_MAP_LOCK = new ReentrantLock();

    /**
     * 编码解器
     */
    private Coder coder = new CoderDefault();

    /**
     * 总线
     */
    private final Supplier<DamiBus> busSupplier;

    public DamiLpcImpl() {
        this(() -> Dami.bus());
    }

    public DamiLpcImpl(DamiBus bus) {
        this(() -> bus);
    }

    public DamiLpcImpl(Supplier<DamiBus> busSupplier) {
        this.busSupplier = busSupplier;
    }


    /**
     * 设置编码器
     *
     * @param coder 编码器
     */
    @Override
    public DamiLpcConfigurator coder(Coder coder) {
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


    /// ///////

    /**
     * 创建服务消费者（接口代理）
     *
     * @param topicMapping 主题映射
     * @param consumerApi  消费者接口
     */
    @Override
    public <T> T createConsumer(String topicMapping, Class<T> consumerApi) {
        Object tmp = Proxy.newProxyInstance(consumerApi.getClassLoader(), new Class[]{consumerApi}, new ConsumerInvocationHandler(this, consumerApi, topicMapping));

        if (log.isDebugEnabled()) {
            log.debug("This sender created successfully(@{}.*): {}", topicMapping, consumerApi.getName());
        }

        return (T) tmp;
    }

    /**
     * 注册服务实现（一个服务，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param serviceObj   服务实现类
     */
    @Override
    public void registerService(String topicMapping, int index, Object serviceObj) {
        Class<?> serviceClz = serviceObj.getClass();

        SERVICE_MAP_LOCK.lock();
        try {
            //防止重复注册
            if (serviceMap.containsKey(serviceClz)) {
                throw new DamiException("This listener is registered: " + serviceClz.getName());
            }

            //开始注册
            List<TopicListenRecord> listenerRecords = new ArrayList<>();

            for (Method m1 : findMethods(serviceClz)) {
                //不能是 Object 申明的方法
                if (m1.getDeclaringClass() != Object.class) {
                    String topic = getMethodTopic(topicMapping, m1.getName());
                    ServiceMethodTopicListener listener = new ServiceMethodTopicListener(this, serviceObj, m1);

                    listenerRecords.add(new TopicListenRecord(topic, listener));
                    bus().listen(topic, index, listener);
                }
            }

            //为了注销时，移掉对应的实例
            serviceMap.put(serviceClz, listenerRecords);
        } finally {
            SERVICE_MAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This listener registered successfully(@{}.*): {}", topicMapping, serviceObj.getClass().getName());
        }
    }

    /**
     * 注销服务实现
     *
     * @param topicMapping 主题映射
     * @param serviceObj   服务实现类
     */
    @Override
    public void unregisterService(String topicMapping, Object serviceObj) {
        SERVICE_MAP_LOCK.lock();
        try {
            List<TopicListenRecord> tmp = serviceMap.remove(serviceObj.getClass());

            if (tmp != null) {
                for (TopicListenRecord r1 : tmp) {
                    bus().unlisten(r1.getTopic(), r1.getListener());
                }
            }
        } finally {
            SERVICE_MAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This listener unregistered successfully(@{}.*): {}", topicMapping, serviceObj.getClass().getName());
        }
    }

    /**
     * 获取方法
     */
    protected Method[] findMethods(Class<?> serviceClz) {
        //只用公有的方法（支持承断） //old::只用自己申明的方法（不支持承断）
        return serviceClz.getMethods();
    }

    /**
     * 获取方法的主题
     */
    protected String getMethodTopic(String topicMapping, String methodName) {
        return topicMapping + "." + methodName;
    }
}
