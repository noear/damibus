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
package org.noear.dami2.bus;

/**
 * 大米总线配置器
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBusConfigurator extends DamiBus {
    /**
     * 配置主题路由器
     */
    DamiBusConfigurator topicRouter(TopicRouter router);

    /**
     * 配置主题调度器
     */
    DamiBusConfigurator topicDispatcher(EventDispatcher dispatcher);

    /**
     * 配置事件负载工厂
     */
    DamiBusConfigurator eventFactory(EventFactory factory);
}