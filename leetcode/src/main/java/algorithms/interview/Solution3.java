package algorithms.interview;

/**
 * URL化。编写一种方法，将字符串中的空格全部替换为%20。假定该字符串尾部有足够的空间存放新增字符，并且知道字符串的“真实”长度。（注：用Java实现的话，请使用字符数组实现，以便直接在数组上操作。）
 *
 * 示例1:
 *
 *  输入："Mr John Smith    ", 13
 *  输出："Mr%20John%20Smith"
 * 示例2:
 *
 *  输入："               ", 5
 *  输出："%20%20%20%20%20"
 * 提示：
 *
 * 字符串长度在[0, 500000]范围内。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/string-to-url-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class Solution3 {
    public String replaceSpaces(String S, int length) {
        if (length > 500000 || length < 0) {
            return S;
        }
        System.out.println(S.length()+","+length);
//        int len = length <= S.length() ? length : S.length();
//        char[] chars = S.toCharArray();
//        for (int i = 0; i < len; i++) {
//            if (chars[i] == ' ') {
//                chars[i] = '%20';
//            }
//        }
        return new String();
    }

    public static void main(String[] args) {
        String str ="               ";
        int length = 5;
        String replaceSpaces = new Solution3().replaceSpaces(str, length);
        System.out.println(replaceSpaces);
    }
}