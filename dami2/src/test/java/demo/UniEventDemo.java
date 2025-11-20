package demo;

import org.noear.dami2.Dami;
import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.bus.receivable.StreamPayload;

/**
 *
 * @author noear 2025/11/20 created
 *
 */
public class UniEventDemo {
    public static void main(String[] args) {
        Dami.bus().listen("", event -> {
            if (event.getPayload() instanceof CallPayload) {
                //is call
                event.<CallPayload>getPayloadAs().getSink().complete("test");
            } else if (event.getPayload() instanceof StreamPayload) {
                //is stream
                event.<StreamPayload>getPayloadAs().getSink().onNext("test");
            } else {
                //is send
            }
        });
    }
}
