package demo_bus;

import org.noear.dami.Dami;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Payload;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;


public class BusStyleDemo {
    static String demo_topic = "demo.user.info";

    public static void main(String[] args) {
        TopicListener<Payload<User, User>> listener = createListener();

        //监听
        Dami.<User, User>bus().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        Dami.<User, User>bus().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<User, User>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                final User content = payload.getContent().sing("你太美");
                //如果是请求载体，再响应一下
                Dami.<User, User>bus().response(payload, content);
            }
        };
    }

    //发送测试
    private static void sendTest() {
        final User user = new User().name("kk").age(2.5).hobby(new String[]{"唱", "跳", "rap", "打篮球"});
        //普通发送
        Dami.<User, Void>bus().send(demo_topic, user);

        //普通发送,自定义构建参数
        Dami.<User, Void>bus().send(new Payload<>("123", demo_topic, user));

        //请求并等响应
        User rst1 = Dami.<User, User>bus().requestAndResponse(demo_topic, user);
        System.out.println("响应返回: " + rst1);

        user.sing("ai kun");
        //请求并等回调
        Dami.<User, User>bus().requestAndCallback(demo_topic, user, rst2 -> {
            System.out.println("响应回调: " + rst2);
        });
    }

    static class User {
        private String name;
        private Double age;
        private String[] hobby;
        private String sing;

        public String sign() {
            return sing;
        }

        public User sing(final String sing) {
            this.sing = sing;
            return this;
        }

        public String name() {
            return name;
        }

        public User name(final String name) {
            this.name = name;
            return this;
        }

        public Double age() {
            return age;
        }

        public User age(final Double age) {
            this.age = age;
            return this;
        }

        public String[] hobby() {
            return hobby;
        }

        public User hobby(final String[] hobby) {
            this.hobby = hobby;
            return this;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final User user = (User) o;
            return Objects.equals(name, user.name) && Objects.equals(age, user.age) && Arrays.equals(hobby, user.hobby) && Objects.equals(sing, user.sing);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, age, sing);
            result = 31 * result + Arrays.hashCode(hobby);
            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("age=" + age)
                    .add("hobby=" + Arrays.toString(hobby))
                    .add("sign='" + sing + "'")
                    .toString();
        }
    }
}