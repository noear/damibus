package benchmark;

import org.noear.dami2.Dami;
import org.noear.dami2.DamiConfig;
import org.noear.dami2.bus.route.PathEventRouter;
import org.noear.dami2.bus.route.PathRouting;

/**
 * @author noear 2025/9/19 created
 */
public class RouterPathTest {
    static int count = 0;

    public static void main(String[] args) {
        DamiConfig.configure(new PathEventRouter());

        Dami.bus().listen("test.*", e -> {
            count = count + 1;
        });

        //不预热，直接开始
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            Dami.bus().send("test.demo", "1");
        }
        System.out.println(System.currentTimeMillis() - start + "::" + count);
    }
}
