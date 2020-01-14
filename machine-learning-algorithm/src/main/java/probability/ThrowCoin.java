package probability;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ThrowCoin {

    public void flipCoin(int n) {
        Map<Integer, Integer> m = Stream.generate(() -> (int) (Math.random() * 2)).limit(n)
                .collect(Collectors.toMap(x -> x, Function.identity(), (x1, x2) -> (x1 == 0 ? 1 : x1) + (x2 == 0 ? 1 : x2)));
        System.out.println(m);
        Collection<Integer> values = m.values();
        double count = 0;
        for (Integer v : values) {
            count += v.intValue();
        }

        for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
            System.out.println(entry.getKey() + "--->" + (entry.getValue() / count));
        }
    }

    public static void main(String[] args) {
        new ThrowCoin().flipCoin(10000);
    }
}
