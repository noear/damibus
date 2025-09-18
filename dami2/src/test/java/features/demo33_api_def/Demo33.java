package features.demo33_api_def;

import org.junit.jupiter.api.Test;
import org.noear.dami2.annotation.Param;
import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.lpc.DamiLpcImpl;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.DamiBusImpl;
import org.noear.dami2.exception.DamiException;

public class Demo33 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.lpc()
    DamiBus bus = new DamiBusImpl();
    DamiLpc api = new DamiLpcImpl(bus);

    @Test
    public void main() {
        api.registerService(topicMapping, new EventDemoImpl());
        EventDemo eventDemo = api.createConsumer(topicMapping, EventDemo.class);
        assert eventDemo.demo1() == 1; //有默认返回值
        eventDemo.demo2();

        try {
            eventDemo.demo3();
            assert false;
        } catch (DamiException e) {
            assert true;
        }

        assert eventDemo.demo4(1, 0) == 5;
    }

    public static interface EventDemo {
        default int demo1() {
            return 1;
        }

        default void demo2() {
        }

        String demo3();

        default int demo4(@Param("b1") Integer i, @Param("b0") Integer b) {
            return 1 + i;
        }
    }

    public static class EventDemoImpl {
        public int demo4(@Param("b0") Integer b, @Param("b1") Integer i) {
            return 4 + i;
        }
    }
}
