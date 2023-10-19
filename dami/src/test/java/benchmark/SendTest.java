package benchmark;

import org.noear.dami.Dami;

public class SendTest {
    public static void main(String[] args) {
        Dami.bus().listen("test.demo", e -> {

        });

        int count = 1000 * 1000 * 10;
        long start = System.currentTimeMillis();
        for (int i = 1; i < count; i++) {
            Dami.bus().send("test.demo", "1");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    static void empty() {

    }
}
