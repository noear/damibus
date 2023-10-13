package org.noear.dami.bus.impl;

import org.noear.dami.bus.*;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
public class TopicDispatcherDefault<C,R> implements TopicDispatcher<C,R> ,Interceptor<C,R> {
    static final Logger log = LoggerFactory.getLogger(TopicDispatcherDefault.class);
    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();

    public TopicDispatcherDefault() {
        interceptors.add(new InterceptorEntity(Integer.MAX_VALUE, this));
    }


    /**
     * 添加拦截器
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    @Override
    public synchronized void addInterceptor(int index, Interceptor interceptor) {
        interceptors.add(new InterceptorEntity(index, interceptor));

        if (interceptors.size() > 1) {
            //排序（顺排）
            interceptors.sort(Comparator.comparing(x -> x.getIndex()));
        }

        if (log.isDebugEnabled()) {
            log.debug("TopicDispatcher interceptor added: {}", interceptor.getClass().getName());
        }
    }

    @Override
    public void handle(Payload<C, R> payload, TopicRouter<C, R> router) {
        AssertUtil.assertTopic(payload.getTopic());

        MDC.put("dami-guid", payload.getGuid());

        List<TopicListenerHolder<C, R>> targets = router.matching(payload.getTopic());

        new InterceptorChain<>(interceptors, targets).doIntercept(payload);
    }

    @Override
    public void doIntercept(Payload<C, R> payload, InterceptorChain<C, R> chain) {
        if (log.isTraceEnabled()) {
            log.trace("{}", payload);
        }

        final List<TopicListenerHolder<C, R>> targets = chain.getTargets();

        if (targets != null && targets.size() > 0) {
            try {
                doExchange(payload, chain.getTargets());
                payload.setHandled();
            } catch (InvocationTargetException e) {
                throw new DamiException(e.getTargetException());
            } catch (UndeclaredThrowableException e) {
                throw new DamiException(e.getUndeclaredThrowable());
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("There's no matching listening on the topic(@{})", payload.getTopic());
            }
        }
    }

    protected void doExchange(Payload<C, R> payload, List<TopicListenerHolder<C, R>> targets) throws Throwable {
        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            targets.get(i).getListener().onEvent(payload);
        }
    }
}
