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

import java.util.regex.Pattern;

/**
 * 监听路由记录（path 模式）
 */
public class RoutingPath<P> extends Routing<P> {

    private final Pattern pattern;

    /**
     * @param expr     表达式（* 表示一段，** 表示不限段）
     * @param index    顺序位
     * @param listener 监听器
     */
    public RoutingPath(String expr, int index, EventListener<P> listener) {
        super(expr, index, listener);

        if (expr.contains("*")) {
            expr = expr.replace(".", "\\."); //支持 . 或 / 做为隔断

            //替换中间的**值
            expr = expr.replace("**", ".[]");

            //替换*值
            expr = expr.replace("*", "[^/\\.]*");

            //替换**值
            expr = expr.replace(".[]", ".*");

            //加头尾
            expr = "^" + expr + "$";

            this.pattern = Pattern.compile(expr);
        } else {
            this.pattern = null;
        }
    }

    @Override
    public boolean isPatterned() {
        return getExpr().indexOf('*') >= 0;
    }

    /**
     * 匹配
     *
     * @param sentTopic 发送的主题
     */
    public boolean matches(String sentTopic) {
        if (super.matches(sentTopic)) {
            return true;
        }

        if (pattern != null) {
            return pattern.matcher(sentTopic).find();
        }

        return false;
    }
}