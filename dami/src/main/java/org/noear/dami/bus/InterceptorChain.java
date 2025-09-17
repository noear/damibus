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
package org.noear.dami.bus;

import java.util.List;

/**
 * 拦截链
 *
 * @author noear
 * @since 1.0
 */
public class InterceptorChain<P> {
    private final List<InterceptorEntity> interceptors;
    private final List<TopicListenerHolder> targets;
    private int interceptorIndex = 0;
    public InterceptorChain(List<InterceptorEntity> interceptors, List<TopicListenerHolder> targets){
        this.interceptors = interceptors;
        this.targets = targets;
    }

    public List<TopicListenerHolder> getTargets() {
        return targets;
    }

    /**
     * 拦截
     * */
    public void doIntercept(Message<P> message) {
        interceptors.get(interceptorIndex++).doIntercept(message, this);
    }
}
