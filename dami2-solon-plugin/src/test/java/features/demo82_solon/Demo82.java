package features.demo82_solon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SolonTest
public class Demo82 {
    static final Logger log = LoggerFactory.getLogger(Demo82.class);
    @Inject
    EventUserService eventUserService;

    @Test
    public void main() throws Throwable {
        User user = eventUserService.getUser(99);
        Assertions.assertEquals(99, user.getUserId());

        user = Dami.bus().<Map, User>call("demo82.event.user.getUser", Dami.asMap("uid", 99)).get();
        Assertions.assertEquals(99, user.getUserId());
    }
}
