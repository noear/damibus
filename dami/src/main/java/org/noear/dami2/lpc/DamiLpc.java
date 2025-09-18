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

import org.noear.dami2.bus.DamiBusExtension;

/**
 * 大米本地过程调用（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public interface DamiLpc extends DamiBusExtension {
    /**
     * 获取编码器
     */
    Coder coder();


    /// ////////////////

    /**
     * 创建服务消费者（接口代理）
     *
     * @param topicMapping 主题映射
     * @param consumerApi  消费者接口
     */
    <T> T createConsumer(String topicMapping, Class<T> consumerApi);

    /**
     * 注册服务实现（一个服务，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param serviceObj   服务实现类
     */
    default void registerService(String topicMapping, Object serviceObj) {
        registerService(topicMapping, 0, serviceObj);
    }

    /**
     * 注册服务实现（一个服务，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param serviceObj   服务实现类
     */
    void registerService(String topicMapping, int index, Object serviceObj);

    /**
     * 注销服务实现
     *
     * @param topicMapping 主题映射
     * @param serviceObj   服务实现类
     */
    void unregisterService(String topicMapping, Object serviceObj);
}