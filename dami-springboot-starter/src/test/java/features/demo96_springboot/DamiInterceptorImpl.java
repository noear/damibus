package features.demo96_springboot;

import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.InterceptorChain;
import org.noear.dami.bus.Message;
import org.springframework.stereotype.Component;

@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Message message, InterceptorChain chain) {
        System.out.println("拦截：" + message);
        chain.doIntercept(message);
    }
}
