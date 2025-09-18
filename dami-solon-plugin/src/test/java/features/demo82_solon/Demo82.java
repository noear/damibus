package features.demo82_solon;

import org.junit.jupiter.api.Test;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SolonTest
public class Demo82 {
    static final Logger log = LoggerFactory.getLogger(Demo82.class);
    @Inject
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        log.debug("{}", user);

        assert user.getUserId() == 99;
    }
}
