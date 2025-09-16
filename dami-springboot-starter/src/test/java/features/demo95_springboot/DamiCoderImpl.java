package features.demo95_springboot;

import org.noear.dami.api.Coder;
import org.noear.dami.api.CoderDefault;
import org.noear.dami.bus.Message;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DamiCoderImpl implements Coder {
    Coder coder = new CoderDefault();

    @Override
    public Object encode(Method method, Object[] args) throws Throwable {
        System.out.println("编码");
        return coder.encode(method, args);
    }

    @Override
    public Object[] decode(Method method, Message message) throws Throwable {
        System.out.println("要解码了");
        return coder.decode(method, message);
    }
}
