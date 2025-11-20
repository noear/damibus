package demo;

import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.bus.receivable.StreamPayload;

/**
 *
 * @author noear 2025/11/20 created
 *
 */
public class UniEventListener implements EventListener<Object> {
    @Override
    public void onEvent(Event<Object> event) throws Throwable {
        if (event.getPayload() instanceof CallPayload) {
            //is call
            System.err.println(event.<CallPayload>getPayloadAs().getData());
            event.<CallPayload>getPayloadAs().getSink().complete("hi!");
        } else if (event.getPayload() instanceof StreamPayload) {
            //is stream
            System.err.println(event.<StreamPayload<String,String>>getPayloadAs().getData());
            event.<StreamPayload<String,String>>getPayloadAs().getSink().onNext("hi");
            event.<StreamPayload<String,String>>getPayloadAs().getSink().onComplete();
        } else {
            //is send
        }
    }
}
