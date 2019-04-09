import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gbs on 19/4/4.
 */
public class TestMain {

    @Test
    public void test1(){
        List<Integer> integers = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).collect(Collectors.toList());

        for(int i = 0;i<integers.size();i++){
            System.out.println(i);
            System.out.println("-->");
        }
    }
}
