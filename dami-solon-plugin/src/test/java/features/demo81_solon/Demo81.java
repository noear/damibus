package features.demo81_solon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonJUnit5Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SolonJUnit5Extension.class)
public class Demo81 {
    static final Logger log = LoggerFactory.getLogger(Demo81.class);
    @Inject
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        log.debug("{}", user);

        assert user.getUserId() == 99;
    }
}
