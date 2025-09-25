package features.demo92_springboot;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.dami2.Dami;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@ContextConfiguration
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan("features.demo92_springboot")
public class Demo92 {
    @Autowired
    EventUserService eventUserService;

    @Test
    public void main() throws Exception{
        User user = eventUserService.getUser(99);
        assert (99 == user.getUserId());

        user = Dami.bus().<Map, User>call("demo92.event.user.getUser", Dami.asMap("uid", 99)).get();
        assert (99 == user.getUserId());
    }
}
