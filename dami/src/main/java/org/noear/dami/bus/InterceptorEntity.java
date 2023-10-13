package org.noear.dami.bus;

/**
 * 拦截器实体。存放拦截器和顺序位
 *
 * @author noear
 * @since 1.0
 */
public class InterceptorEntity<C, R> implements Interceptor<C,R> {
    /**
     * 顺排序位（排完后，按先进后出策略执行）
     */
    private final int index;
    private final Interceptor<C, R> real;

    public InterceptorEntity(int index, Interceptor<C, R> real) {
        this.index = index;
        this.real = real;
    }

    /**
     * 获取顺序位
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取原拦截器
     */
    public Interceptor<C, R> getReal() {
        return real;
    }

    /**
     * 拦截
     */
    @Override
    public void doIntercept(Payload<C, R> payload, InterceptorChain<C, R> chain) {
        real.doIntercept(payload, chain);
    }
}
