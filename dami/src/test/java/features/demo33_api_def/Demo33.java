package features.demo33_api_def;

import org.junit.jupiter.api.Test;
import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.exception.DamiException;

public class Demo33 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.api()
    DamiBus bus = new DamiBusImpl();
    DamiApi api = new DamiApiImpl(bus);

    @Test
    public void main() {
        api.registerListener(topicMapping,new EventDemoImpl());
        EventDemo eventDemo = api.createSender(topicMapping, EventDemo.class);
        assert eventDemo.demo1() == 1; //有默认返回值
        eventDemo.demo2();

        try {
            eventDemo.demo3();
            assert false;
        } catch (DamiException e) {
            assert true;
        }

        assert eventDemo.demo4() == 4;
    }

    public static interface EventDemo {
        default int demo1() {
            return 1;
        }

        default void demo2() {
        }

        String demo3();

        default int demo4() {
            return 1;
        }
    }

    public static class  EventDemoImpl{
        public int demo4() {
            return 4;
        }
    }
}
