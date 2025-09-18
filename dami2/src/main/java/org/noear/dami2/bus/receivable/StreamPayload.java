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

import org.reactivestreams.Subscriber;

/**
 * 生成流荷载
 *
 * @author noear
 * @since 2.0
 */
public class StreamPayload<D, R> extends ReceivablePayload<D, Subscriber<R>> {
    /**
     * @param data     数据
     * @param receiver 接收器
     *
     */
    public StreamPayload(D data, Subscriber<R> receiver) {
        super(data, receiver);
    }

    /**
     * 接收器
     */
    @Override
    public Subscriber<R> getReceiver() {
        return super.getReceiver();
    }
}
