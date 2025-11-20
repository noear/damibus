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
            event.<CallPayload>getPayloadAs().getSink().complete("test");
        } else if (event.getPayload() instanceof StreamPayload) {
            //is stream
            event.<StreamPayload>getPayloadAs().getSink().onNext("test");
        } else {
            //is send
        }
    }
}
