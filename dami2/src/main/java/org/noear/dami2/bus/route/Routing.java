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
package org.noear.dami2.bus.route;

import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;

/**
 * 路由记录
 *
 * @author noear
 * @since 1.0
 * */
public class Routing<P> extends EventListenerHolder<P> {
    private final String expr;

    /**
     * @param expr     监听主题表达式
     * @param index    顺序位
     * @param listener 监听器
     */
    public Routing(String expr, int index, EventListener<P> listener) {
        super(index, listener);
        this.expr = expr;
    }

    /**
     * 有模式化的
     */
    public boolean isPatterned() {
        return false;
    }

    /**
     * 匹配
     *
     * @param sentTopic 发送的主题
     */
    public boolean matches(String sentTopic) {
        return this.getExpr().equals(sentTopic);
    }

    /**
     * 获取表达式
     */
    public String getExpr() {
        return expr;
    }
}
