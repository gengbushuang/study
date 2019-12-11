package algorithms.primary.array;

import java.util.Arrays;

/**
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * <p>
 * 示例 1:
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 */
public class Solution8 {

    public void moveZeroes(int[] nums) {
        if (nums.length < 2) {
            return;
        }
        boolean b = true;
        for (int i = 0; b && i < nums.length; i++) {
            if (nums[i] == 0) {
                b = false;
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[j] != 0) {
                        nums[i] = nums[j];
                        nums[j] = 0;
                        b = true;
                        break;
                    }
                }
            }
        }
    }

    public void moveZeroes2(int[] nums) {
        if (nums.length < 2) {
            return;
        }
        int a = 0;
        int b = 0;
        for (int i = 0; i < nums.length && b < nums.length-1; i++) {
            if (nums[i] == 0) {
                a = b == 0 ? i : b;
                for (int j = a; j < nums.length; j++) {
                    if (nums[j] != 0) {
                        nums[i] = nums[j];
                        nums[j] = 0;
                        b = j;
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {0,1,0, 0,1,0, 1};
        new Solution8().moveZeroes2(nums);
        System.out.println(Arrays.toString(nums));
    }
}
