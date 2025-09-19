package benchmark;

import org.noear.dami2.Dami;

public class SendTest {
    static int count = 0;

    public static void main(String[] args) {

        Dami.bus().listen("test.demo", e -> {
            count = count + 1;
        });

        //不预热，直接开始
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) {
            Dami.bus().send("test.demo", "1");
        }
        System.out.println(System.currentTimeMillis() - start + "::" + count);
    }
}
