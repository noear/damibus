package features.demo91_springboot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootTest(classes = Demo91.class)
@ComponentScan("features.demo91_springboot")
public class Demo91 {
    @Autowired
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        assert user.getUserId() == 99;
    }
}
