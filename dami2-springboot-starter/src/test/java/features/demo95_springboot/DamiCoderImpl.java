package features.demo95_springboot;

import org.noear.dami2.lpc.Coder;
import org.noear.dami2.lpc.CoderForName;
import org.noear.dami2.bus.Event;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DamiCoderImpl implements Coder {
    Coder coder = new CoderForName();

    @Override
    public Object encode(Method method, Object[] args) throws Throwable {
        System.out.println("编码");
        return coder.encode(method, args);
    }

    @Override
    public Object[] decode(Method method, Event event) throws Throwable {
        System.out.println("要解码了");
        return coder.decode(method, event);
    }
}
