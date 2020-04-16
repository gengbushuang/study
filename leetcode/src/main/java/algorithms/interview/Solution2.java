package algorithms.interview;

import java.util.Arrays;

/**
 * 给定两个字符串 s1 和 s2，请编写一个程序，确定其中一个字符串的字符重新排列后，能否变成另一个字符串。
 * <p>
 * 示例 1：
 * <p>
 * 输入: s1 = "abc", s2 = "bca"
 * 输出: true
 * 示例 2：
 * <p>
 * 输入: s1 = "abc", s2 = "bad"
 * 输出: false
 * 说明：
 * <p>
 * 0 <= len(s1) <= 100
 * 0 <= len(s2) <= 100
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/check-permutation-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class Solution2 {
    public boolean CheckPermutation(String s1, String s2) {
        int lenS1 = s1.length();
        int lenS2 = s2.length();
        if (lenS1 == lenS2 && lenS1 >= 0 && lenS1 <= 100) {
            char[] charsS1 = s1.toCharArray();
            char[] charsS2 = s2.toCharArray();
            Arrays.sort(charsS1);
            Arrays.sort(charsS2);
            for (int i = 0; i < lenS1; i++) {
                if (charsS1[i] != charsS2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String s1="abc";
        String s2="bca";
        boolean checkPermutation = new Solution2().CheckPermutation(s1, s2);
        System.out.println(checkPermutation);
    }
}