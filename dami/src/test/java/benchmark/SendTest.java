package benchmark;

import org.noear.dami.Dami;

public class SendTest {
    static Integer count = 0;

    public static void main(String[] args) {

        Dami.bus().listen("test.demo", e -> {
            count = count + 1;
        });

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            Dami.bus().send("test.demo", "1");
        }
        System.out.println(System.currentTimeMillis() - start + "::" + count);
    }
}
