package org.algorithms.java.string;

import org.algorithms.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LSD {

    public static void sort(String[] a, int w) {
        int n = a.length;
        int R = 256;

        String[] aux = new String[n];

        for (int d = w - 1; d >= 0; d--) {
            int[] count = new int[R + 1];
            for (int i = 0; i < n; i++) {
                System.out.println(a[i].charAt(d) + "," + (a[i].charAt(d) + 1));
                count[a[i].charAt(d) + 1]++;
            }

            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            for (int i = 0; i < n; i++) {
                aux[count[a[i].charAt(d)]++] = a[i];
            }
            System.out.println(Arrays.toString(aux));
        }
    }

    public static void main(String[] args) {
        List<String> strings = Utils.readAllStrings("string/words3.txt", x -> x.trim());
        String[] arrays = strings.stream().flatMap(x -> Stream.of(x.split(" "))).toArray(String[]::new);
        System.out.println(Arrays.toString(arrays));
        sort(arrays, 3);
    }
}
