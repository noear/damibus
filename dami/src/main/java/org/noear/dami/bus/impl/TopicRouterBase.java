package org.noear.dami.bus.impl;

import org.noear.dami.bus.*;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 *
 * @author noear
 * @since 1.0
 */
public abstract class TopicRouterBase<C,R> implements TopicRouter<C, R>, Interceptor<C,R> {
    static final Logger baseLog = LoggerFactory.getLogger(TopicRouterBase.class);

    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();

    public TopicRouterBase() {
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

        if (baseLog.isDebugEnabled()) {
            baseLog.debug("TopicRouter interceptor added: {}", interceptor.getClass().getName());
        }

    }

    /**
     * 接收事件
     *
     * @param payload 事件负载
     */
    @Override
    public void handle(final Payload<C, R> payload) {
        MDC.put("dami-guid", payload.getGuid());

        new InterceptorChain<C, R>(interceptors).doIntercept(payload);
    }

    /**
     * 断言主题是否为空
     *
     * @param topic
     */
    protected void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new DamiException("The topic cannot be empty");
        }
    }
}
