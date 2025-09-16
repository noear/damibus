package org.noear.dami.bus;

import java.util.List;

/**
 * 拦截链
 *
 * @author noear
 * @since 1.0
 */
public class InterceptorChain<C> {
    private final List<InterceptorEntity> interceptors;
    private final List<TopicListenerHolder<C>> targets;
    private int interceptorIndex = 0;
    public InterceptorChain(List<InterceptorEntity> interceptors, List<TopicListenerHolder<C>> targets){
        this.interceptors = interceptors;
        this.targets = targets;
    }

    public List<TopicListenerHolder<C>> getTargets() {
        return targets;
    }

    /**
     * 拦截
     * */
    public void doIntercept(Message<C> payload) {
        interceptors.get(interceptorIndex++).doIntercept(payload, this);
    }
}
