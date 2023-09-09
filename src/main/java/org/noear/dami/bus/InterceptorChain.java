package org.noear.dami.bus;

import java.util.List;

/**
 * 拦截链
 *
 * @author noear
 * @since 1.0
 */
public class InterceptorChain<C, R> {
    private final List<InterceptorEntity> interceptors;
    private int interceptorIndex = 0;
    public InterceptorChain(List<InterceptorEntity> interceptors){
        this.interceptors = interceptors;
    }

    /**
     * 拦截
     * */
    public void doIntercept(Payload<C, R> payload) {
        interceptors.get(interceptorIndex++).doIntercept(payload, this);
    }
}
