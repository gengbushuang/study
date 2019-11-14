package algorithms.primary.array;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * <p>
 * 示例 1:
 * 输入: [4,1,2,1,2]
 * 输出: 4
 */
public class Solution5 {

    public int singleNumber(int[] nums) {
        if(nums.length==1){
            return nums[0];
        }
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] == nums[i]) {
                i++;
                continue;
            }
            return nums[i - 1];
        }
        return nums[nums.length-1];
    }

    public static void main(String[] args) {
        int[] nums = {4,1,2,1,2};
        int number = new Solution5().singleNumber(nums);
        System.out.println(number);
    }
}
