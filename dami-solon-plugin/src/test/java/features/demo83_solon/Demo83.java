package features.demo83_solon;

import org.junit.jupiter.api.Test;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;

@SolonTest
public class Demo83 {
    @Inject
    EventUserNotices eventUserNotices;

    @Test
    public void main() {
        eventUserNotices.onCreated(82, "noear");
    }
}
