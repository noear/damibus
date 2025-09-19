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
package org.noear.dami2.lpc;

import org.noear.dami2.lpc.impl.ProviderMethodEventListener;
import org.noear.dami2.lpc.impl.TopicListenRecord;
import org.noear.dami2.lpc.impl.ConsumerInvocationHandler;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.exception.DamiException;
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
    private Map<Class<?>, List<TopicListenRecord>> providerMap = new HashMap<>();
    private ReentrantLock PROVIDER_MAP_LOCK = new ReentrantLock();

    /**
     * 编码解器
     */
    private Coder coder = new CoderDefault();

    /**
     * 总线
     */
    private final Supplier<DamiBus> busSupplier;

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
            log.debug("This consumer created successfully(@{}.*): {}", topicMapping, consumerApi.getName());
        }

        return (T) tmp;
    }

    /**
     * 注册服务提供者（一个服务，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param roviderObj   提供者对象
     */
    @Override
    public void registerProvider(String topicMapping, int index, Object roviderObj) {
        Class<?> roviderClz = roviderObj.getClass();

        PROVIDER_MAP_LOCK.lock();
        try {
            //防止重复注册
            if (providerMap.containsKey(roviderClz)) {
                throw new DamiException("This listener is registered: " + roviderClz.getName());
            }

            //开始注册
            List<TopicListenRecord> listenerRecords = new ArrayList<>();

            for (Method m1 : findMethods(roviderClz)) {
                //不能是 Object 申明的方法
                if (m1.getDeclaringClass() != Object.class) {
                    String topic = getMethodTopic(topicMapping, m1.getName());
                    ProviderMethodEventListener listener = new ProviderMethodEventListener(this, roviderObj, m1);

                    listenerRecords.add(new TopicListenRecord(topic, listener));
                    bus().listen(topic, index, listener);
                }
            }

            //为了注销时，移掉对应的实例
            providerMap.put(roviderClz, listenerRecords);
        } finally {
            PROVIDER_MAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This provider registered successfully(@{}.*): {}", topicMapping, roviderObj.getClass().getName());
        }
    }

    /**
     * 注销服务提供者
     *
     * @param topicMapping 主题映射
     * @param roviderObj   提供者对象
     */
    @Override
    public void unregisterProvider(String topicMapping, Object roviderObj) {
        PROVIDER_MAP_LOCK.lock();
        try {
            List<TopicListenRecord> tmp = providerMap.remove(roviderObj.getClass());

            if (tmp != null) {
                for (TopicListenRecord r1 : tmp) {
                    bus().unlisten(r1.getTopic(), r1.getListener());
                }
            }
        } finally {
            PROVIDER_MAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("This provider unregistered successfully(@{}.*): {}", topicMapping, roviderObj.getClass().getName());
        }
    }

    /**
     * 获取方法
     */
    protected Method[] findMethods(Class<?> roviderClz) {
        //只用公有的方法（支持承断） //old::只用自己申明的方法（不支持承断）
        return roviderClz.getMethods();
    }

    /**
     * 获取方法的主题
     */
    protected String getMethodTopic(String topicMapping, String methodName) {
        return topicMapping + "." + methodName;
    }
}
