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
package org.noear.dami2.bus.impl;

import org.noear.dami2.bus.*;
import org.noear.dami2.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 事件调度器默认实现
 *
 * @author noear
 * @since 1.0
 */
public class EventDispatcherDefault implements EventDispatcher,Interceptor {
    static final Logger log = LoggerFactory.getLogger(EventDispatcherDefault.class);
    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();
    private final ReentrantLock INTERCEPTORS_LOCK = new ReentrantLock();

    public EventDispatcherDefault() {
        interceptors.add(new InterceptorEntity(Integer.MAX_VALUE, this));
    }


    /**
     * 添加拦截器
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    @Override
    public void addInterceptor(int index, Interceptor interceptor) {
        INTERCEPTORS_LOCK.lock();
        try {
            interceptors.add(new InterceptorEntity(index, interceptor));

            if (interceptors.size() > 1) {
                //排序（顺排）
                interceptors.sort(Comparator.comparing(x -> x.getIndex()));
            }
        } finally {
            INTERCEPTORS_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("TopicDispatcher interceptor added: {}", interceptor.getClass().getName());
        }
    }


    /**
     * 执行拦截
     */
    @Override
    public void doIntercept(Event event, InterceptorChain chain) {
        if (log.isTraceEnabled()) {
            log.trace("{}", event);
        }

        final List<TopicListenerHolder> targets = chain.getTargets();

        if (targets != null && targets.size() > 0) {
            try {
                doDispatch(event, chain.getTargets());
                event.setHandled();
            } catch (InvocationTargetException e) {
                throw new DamiException(e.getTargetException());
            } catch (UndeclaredThrowableException e) {
                throw new DamiException(e.getUndeclaredThrowable());
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("There's no matching listener on the topic(@{})", event.getTopic());
            }
        }
    }

    /**
     * 派发
     */
    @Override
    public void dispatch(Event event, TopicRouter router) {
        AssertUtil.assertTopic(event.getTopic());

        //获取路由匹配结果
        List<TopicListenerHolder> targets = router.matching(event.getTopic());

        //转成拦截链处理
        new InterceptorChain<>(interceptors, targets).doIntercept(event);

    }

    /**
     * 执行派发
     */
    protected void doDispatch(Event event, List<TopicListenerHolder> targets) throws Throwable {
        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            targets.get(i).getListener().onEvent(event);
        }
    }
}