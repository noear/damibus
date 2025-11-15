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
package org.noear.dami2.bus.receivable;

import org.noear.dami2.exception.DamiException;

import java.util.concurrent.CompletableFuture;

/**
 * 调用荷载
 *
 * @author noear
 * @since 2.0
 */
public class CallPayload<D,R> extends ReceivablePayload<D, CompletableFuture<R>> {
    /**
     * @param data 数据
     */
    public CallPayload(D data) {
        super(data, new CompletableFuture<>());
    }

    /**
     * @param data 数据
     * @param sink 接收器
     */
    public CallPayload(D data, CompletableFuture<R> sink) {
        super(data, sink);
    }

    /**
     * 接收器
     */
    @Override
    public CompletableFuture<R> getSink() {
        return super.getSink();
    }

    /**
     * 当异常时
     */
    @Override
    public void onError(Throwable e) {
        getSink().completeExceptionally(e);
    }
}