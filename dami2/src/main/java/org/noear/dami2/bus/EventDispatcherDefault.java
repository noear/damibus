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

import org.noear.dami2.bus.intercept.EventInterceptor;
import org.noear.dami2.bus.intercept.InterceptorChain;
import org.noear.dami2.bus.intercept.InterceptorEntity;
import org.noear.dami2.bus.receivable.ReceivablePayload;
import org.noear.dami2.bus.route.EventRouter;
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
public class EventDispatcherDefault implements EventDispatcher {
    static final Logger log = LoggerFactory.getLogger(EventDispatcherDefault.class);
    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();
    private final ReentrantLock INTERCEPTORS_LOCK = new ReentrantLock();

    /**
     * 添加拦截器
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    @Override
    public void addInterceptor(int index, EventInterceptor interceptor) {
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
     * 派发
     */
    @Override
    public void dispatch(Event event, EventRouter router) {
        //调度流程：1.拦截；2.预检；3.分发

        AssertUtil.assertTopic(event.getTopic());

        //获取路由匹配结果
        List<EventListenerHolder> targets = router.matching(event.getTopic());

        //转成拦截链处理
        new InterceptorChain<>(interceptors, targets, this::doPrecheck).doIntercept(event);

    }


    /**
     * 执行预检
     */
    protected void doPrecheck(Event event, InterceptorChain chain) {
        if (log.isTraceEnabled()) {
            log.trace("{}", event);
        }

        final List<EventListenerHolder> targets = chain.getTargets();

        if (targets != null && targets.size() > 0) {
            try {
                doDistribute(event, chain.getTargets());
            } catch (Throwable e) {
                if (e instanceof InvocationTargetException) {
                    e = ((InvocationTargetException) e).getTargetException();
                } else if (e instanceof UndeclaredThrowableException) {
                    e = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
                }

                if (event.getPayload() instanceof ReceivablePayload) {
                    ((ReceivablePayload) event.getPayload()).onError(new DamiException(e));
                } else {
                    throw new DamiException(e);
                }
            } finally {
                //说明是有路由目标的（不触发备用处理）
                event.setHandled();
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("There's no matching listener on the topic(@{})", event.getTopic());
            }
        }
    }

    /**
     * 执行分发
     */
    protected void doDistribute(Event event, List<EventListenerHolder> targets) throws Throwable {
        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            targets.get(i).getListener().onEvent(event);
        }
    }
}