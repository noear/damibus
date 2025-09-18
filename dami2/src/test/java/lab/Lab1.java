package lab;

import reactor.core.publisher.Flux;

/**
 *
 * @author noear 2025/9/17 created
 *
 */
public class Lab1 {
    public static void main(String[] args) {
        Flux.create(fluxSink -> {
            fluxSink.next("");
            fluxSink.complete();
            fluxSink.error(new RuntimeException("error"));

            ;
            fluxSink.isCancelled();

            fluxSink.onRequest(n -> {
                System.out.println(Thread.currentThread());
                System.out.println(n);
            });

            fluxSink.onCancel(() -> {
                System.out.println(Thread.currentThread());
            });
        });
    }
}
