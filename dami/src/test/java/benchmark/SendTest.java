package benchmark;

import org.noear.dami.Dami;

public class SendTest {
    public static void main(String[] args) {
        Dami.bus().listen("test.demo", e -> { });

        long start = System.currentTimeMillis();
        for (int i = 1; i < 10000000; i++) {
            Dami.bus().send("test.demo", "1");
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
