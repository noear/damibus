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
 * 拦截器实体。存放拦截器和顺序位
 *
 * @author noear
 * @since 1.0
 */
public class InterceptorEntity<P> implements Interceptor<P> {
    /**
     * 顺排序位（排完后，按先进后出策略执行）
     */
    private final int index;
    private final Interceptor<P> real;

    public InterceptorEntity(int index, Interceptor<P> real) {
        this.index = index;
        this.real = real;
    }

    /**
     * 获取顺序位
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取原拦截器
     */
    public Interceptor<P> getReal() {
        return real;
    }

    /**
     * 拦截
     */
    @Override
    public void doIntercept(Event<P> event, InterceptorChain<P> chain) {
        real.doIntercept(event, chain);
    }
}