package benchmark;

import org.noear.dami2.Dami;
import org.noear.dami2.DamiConfig;
import org.noear.dami2.bus.route.PathEventRouter;
import org.noear.dami2.bus.route.TagEventRouter;
import org.noear.dami2.bus.route.TagRouting;

/**
 * @author noear 2025/9/19 created
 */
public class RouterTagTest {
    static int count = 0;

    public static void main(String[] args) {
        DamiConfig.configure(new TagEventRouter());

        Dami.bus().listen("test:a", e -> {
            count = count + 1;
        });

        //不预热，直接开始
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) {
            Dami.bus().send("test", "1");
        }
        System.out.println(System.currentTimeMillis() - start + "::" + count);
    }
}
