package features.demo92_springboot;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootTest(classes = Demo92.class)
@ComponentScan("features.demo92_springboot")
public class Demo92 {
    @Autowired
    EventUserNotices eventUserNotices;

    @Test
    public void main(){
        eventUserNotices.onCreated(92, "noear");
    }
}
