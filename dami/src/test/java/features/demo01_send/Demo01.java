package features.demo01_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.plus.DamiBusPlusImpl;
import org.noear.dami.bus.plus.DamiBusSml;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo01 {
    //定义实例，避免单测干扰 //开发时用：Dami.busSml()
    DamiBusSml busSml = new DamiBusPlusImpl<>();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busSml.listen(User.class, user -> {
            System.err.println(user);
            testObserver.incrementAndGet();
        });


        //发送事件
        busSml.send(new User("noear"));

        assert testObserver.get() == 1;
    }

    public class User {
        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
