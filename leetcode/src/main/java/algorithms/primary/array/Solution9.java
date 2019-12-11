package algorithms.primary.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * <p>
 * 示例 1:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * <p>
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
public class Solution9 {

    public int[] twoSum(int[] nums, int target) {
        if (nums.length < 2) {
            return new int[0];
        }
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    int[] result = {i, j};
                    return result;
                }
            }
        }
        return new int[0];
    }


    public int[] twoSum2(int[] nums, int target) {
        if (nums.length < 2) {
            return new int[0];
        }
        Map<Integer,Integer> map = new HashMap<>(nums.length);
        for(int i = 0; i < nums.length; i++){
            map.put(nums[i],i);
        }

        for (int i = 0; i < nums.length; i++) {
            int i1 = target - nums[i];
            if (map.containsKey(i1) && map.get(i1).intValue() != i) {
                int[] result = {i, map.get(i1)};
                return result;
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] nums = {-3,4,3,90};
        int target = 0;
        int[] ints = new Solution9().twoSum(nums, target);
        System.out.println(Arrays.toString(ints));
    }
}
