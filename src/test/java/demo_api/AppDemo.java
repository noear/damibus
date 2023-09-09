package demo_api;

import demo_api.mod1.UserEventListenerImpl;
import demo_api.mod2.UserEventSender;
import org.noear.dami.DamiApi;
import org.noear.dami.api.DamiApiImpl;

/**
 * @author noear 2023/9/9 created
 */
public class AppDemo {
    public static void main(String[] args) {
        DamiApi damiApi = new DamiApiImpl();

        damiApi.registerListener("demo.user", new UserEventListenerImpl());
        UserEventSender userEventSender = damiApi.createSender("demo.user", UserEventSender.class);

        userEventSender.created(1,"noear");
        userEventSender.updated(2, "dami");
    }
}
