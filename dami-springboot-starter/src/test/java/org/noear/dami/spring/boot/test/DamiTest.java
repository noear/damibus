package org.noear.dami.spring.boot.test;

import org.noear.dami.spring.boot.test.mod2.UserEventSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DamiTest {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DamiTest.class);
        UserEventSender bean = context.getBean(UserEventSender.class);
        long rst = bean.created(1, "kamo");
        System.out.println("收到返回：" + rst);
        bean.updated(2,"akino");
    }
}
