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
package org.noear.dami.bus.payload;

import org.noear.dami.bus.AssertUtil;

import java.io.Serializable;

/**
 * 可接收核载
 *
 * @author noear
 * @since 2.0
 */
public class ReceivePayload<C,Rec> implements Serializable {
    private final C context;
    private final transient Rec receiver;

    public ReceivePayload(C context, Rec receiver) {
        AssertUtil.notNull(receiver, "The receiver can not be null");

        this.context = context;
        this.receiver = receiver;
    }

    public C getContext() {
        return context;
    }

    public Rec getReceiver() {
        return receiver;
    }
}