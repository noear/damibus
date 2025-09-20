package lab;

import org.noear.dami2.Dami;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.receivable.CallEventListener;
import org.noear.dami2.bus.receivable.StreamEventListener;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author noear 2025/9/20 created
 *
 */
public interface DamiBusV1 {
    static Logger logger = LoggerFactory.getLogger(DamiBusV1.class);

    /**
     * 发送
     *
     * @param <P> 荷载类型
     */
    static <P> void send(String topic, P payload) {
        Dami.bus().send(topic, payload);
    }

    /**
     * 发送并要求一次答复（支持 lpc）
     *
     * @param <D> 发送数据类型
     * @param <R> 响应数据类型
     *
     */
    static <D, R> R sendAndRequest(String topic, D data) throws InterruptedException, ExecutionException {
        return Dami.bus().<D, R>call(topic, data).get();
    }

    /**
     * 发送并要求一次答复（支持 lpc）
     *
     * @param <D>      发送数据类型
     * @param <R>      响应数据类型
     * @param fallback 应急处理（如果没有订阅）
     *
     */
    static <D, R> R sendAndRequest(String topic, D data, Supplier<R> fallback) throws InterruptedException, ExecutionException {
        if (fallback == null) {
            return Dami.bus().<D, R>call(topic, data, r -> {
                r.complete(fallback.get());
            }).get();
        } else {
            return Dami.bus().<D, R>call(topic, data, r -> {
                r.complete(fallback.get());
            }).get();
        }

    }

    /**
     * 发送并要求多次答复（响应式流）
     *
     * @param <D>      发送数据类型
     * @param <R>      响应数据类型
     * @param callback 回调
     *
     */
    static <D, R> void sendAndSubscribe(String topic, D data, Consumer<R> callback) {
        sendAndSubscribe(topic, data, callback, null);
    }

    /**
     *
     * @param <D>      发送数据类型
     * @param <R>      响应数据类型
     * @param callback 回调
     * @param fallback 应急处理（如果没有订阅）
     */
    static <D, R> void sendAndSubscribe(String topic, D data, Consumer<R> callback, Supplier<R> fallback) {
        Subscriber<R> subscriber = new Subscriber<R>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(R r) {
                callback.accept(r);
                if (subscription != null) {
                    subscription.request(1);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {

            }
        };

        if (fallback == null) {
            Dami.bus().<D, R>stream(topic, data).subscribe(subscriber);
        } else {
            Dami.bus().<D, R>stream(topic, data, r -> {
                r.onNext(fallback.get());
                r.onComplete();
            }).subscribe(subscriber);
        }
    }

    /**
     * on send
     *
     * @param <P> 荷载类型
     */
    static <P> void listen(String topic, EventListener<P> listener) {
        Dami.bus().listen(topic, listener);
    }

    /**
     * on sendAndRequest
     *
     * @param <D> 发送数据类型
     * @param <R> 响应数据类型
     */
    static <D, R> void listen(String topic, CallEventListener<D, R> listener) {
        Dami.bus().listen(topic, listener);
    }

    /**
     * on sendAndSubscribe
     *
     * @param <D> 发送数据类型
     * @param <R> 响应数据类型
     */
    static <D, R> void listen(String topic, StreamEventListener<D, R> listener) {
        Dami.bus().listen(topic, listener);
    }

    /// //////////////
}