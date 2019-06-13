package algorithms.primary.array;

import java.util.Arrays;

/**
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 * <p>
 * 示例 1:
 * 输入: [1,2,3,4,5,6,7] 和 k = 3
 * 输出: [5,6,7,1,2,3,4]
 * 解释:
 * 向右旋转 1 步: [7,1,2,3,4,5,6]
 * 向右旋转 2 步: [6,7,1,2,3,4,5]
 * 向右旋转 3 步: [5,6,7,1,2,3,4]
 * <p>
 * [1,2,7,1,5,6,4]
 * [1,6,7,1,5,3,4]
 * [5,6,7,1,2,3,4]
 */
public class Solution3 {

    public void rotate(int[] nums, int k) {
        int len = nums.length;
        int tmp = 0;
        int count = 0;
//        for(int j = 0;j<k;j++) {
//            for (int i = len - 1; i > 0; i--) {
//                count++;
//                tmp = nums[i];
//                nums[i] = nums[i - 1];
//                nums[i - 1] = tmp;
//            }
//        }
        //下面这个是长度为奇数
        int index = 0;
        int start = index;
        tmp = nums[start];
        do {
            count++;
            index += k;
            int end = index % len;
            int tmp1 = nums[end];
            nums[end] = tmp;
            tmp = tmp1;
            start = end;
        } while (index / len < k);
        System.out.println(count);
    }


    public static void main(String[] args) {
        int[] nums = {-1,-100,3,99};
        int k = 2;
        System.out.println(Arrays.toString(nums));
        new Solution3().rotate(nums, k);
        System.out.println(Arrays.toString(nums));
    }
}
