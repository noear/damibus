package lab;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author noear 2025/9/20 created
 *
 */
public class DamiBusV1Demo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final String topic = "demo.hello";

        // for send
        DamiBusV1.<String>listen(topic, event -> {
            System.out.println("Received data: " + event.getPayload());
        });
        DamiBusV1.send(topic, "hi");


        // for sendAndRequest
        DamiBusV1.<String, String>listen(topic, (event, data, sink) -> {
            System.out.println("Received data: " + data);
            sink.complete("hello!"); //sink = CompletableFuture
        });
        DamiBusV1.<String, String>sendAndRequest(topic, "hi");


        // for sendAndSubscribe
        DamiBusV1.<String, String>listen(topic, (event, att, data, sink) -> {
            System.out.println("Received data: " + data);
            sink.onNext("hello!"); //sink = Subscriber
            sink.onNext("miss you!"); //sink = Subscriber
            sink.onComplete();
        });
        DamiBusV1.<String, String>sendAndSubscribe(topic, "hi", item -> {
            System.out.println("Callback data: " + item);
        });
    }
}