package algorithms.primary.array;

import java.util.Arrays;

/**
 * 给定一个由整数组成的非空数组所表示的非负整数，在该数的基础上加一。
 *
 * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
 *
 * 你可以假设除了整数 0 之外，这个整数不会以零开头。
 * <p>
 * 示例 1:
 * 输入: [1,2,3]
 * 输出: [1,2,4]
 */
public class Solution7 {

    public int[] plusOne(int[] digits) {
        if (digits.length < 1) {
            return digits;
        }
        int tmp = 1;
        for (int i = digits.length - 1; i >= 0; --i) {
            if (tmp == 0) {
                continue;
            }
            tmp = digits[i] + tmp;
            if (tmp > 9) {
                digits[i] = tmp - 10;
                tmp = 1;
            } else {
                digits[i] = tmp;
                tmp = 0;
            }
        }

        if (tmp > 0) {
            int[] tmps = new int[digits.length + 1];
            System.arraycopy(digits, 0, tmps, 1, digits.length);
            tmps[0] = tmp;
            return tmps;
        }
        return digits;
    }


    public int[] plusOne2(int[] digits) {
        if (digits.length < 1) {
            return digits;
        }
        for (int i = digits.length - 1; i >= 0; --i) {
            if (digits[i] != 9) {
                digits[i] = digits[i] + 1;
                return digits;
            } else {
                digits[i] = 0;
            }
        }
        int[] tmps = new int[digits.length + 1];
        tmps[0] = 1;
        return tmps;
    }

    public static void main(String[] args) {
        int[] nums = {9};
        int[] ints = new Solution7().plusOne2(nums);
        System.out.println(Arrays.toString(ints));
    }
}
