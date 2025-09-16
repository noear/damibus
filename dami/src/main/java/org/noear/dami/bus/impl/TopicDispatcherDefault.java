package org.noear.dami.bus.impl;

import org.noear.dami.bus.*;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主题派发器默认实现
 *
 * @author noear
 * @since 1.0
 */
public class TopicDispatcherDefault<C> implements TopicDispatcher<C> ,Interceptor<C> {
    static final Logger log = LoggerFactory.getLogger(TopicDispatcherDefault.class);
    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();
    private final ReentrantLock INTERCEPTORS_LOCK = new ReentrantLock();

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
    public void doIntercept(Message<C> message, InterceptorChain<C> chain) {
        if (log.isTraceEnabled()) {
            log.trace("{}", message);
        }

        final List<TopicListenerHolder<C>> targets = chain.getTargets();

        if (targets != null && targets.size() > 0) {
            try {
                doDispatch(message, chain.getTargets());
                message.setHandled();
            } catch (InvocationTargetException e) {
                throw new DamiException(e.getTargetException());
            } catch (UndeclaredThrowableException e) {
                throw new DamiException(e.getUndeclaredThrowable());
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("There's no matching listener on the topic(@{})", message.getTopic());
            }
        }
    }

    /**
     * 派发
     */
    @Override
    public void dispatch(Message<C> message, TopicRouter<C> router) {
        AssertUtil.assertTopic(message.getTopic());

//        try {
//            MDC.put("dami-plid", message.getPlid());

            //获取路由匹配结果
            List<TopicListenerHolder<C>> targets = router.matching(message.getTopic());

            //转成拦截链处理
            new InterceptorChain<>(interceptors, targets).doIntercept(message);
//        } finally {
//            MDC.remove("dami-plid");
//        }
    }

    /**
     * 执行派发
     */
    protected void doDispatch(Message<C> message, List<TopicListenerHolder<C>> targets) throws Throwable {
        //用 i，可以避免遍历时添加监听的异常
        for (int i = 0; i < targets.size(); i++) {
            targets.get(i).getListener().onEvent(message);
        }
    }
}