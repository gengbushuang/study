package algorithms.primary.array;

import java.util.Arrays;
import java.util.Random;

/**
 * 给定一个整数数组，判断是否存在重复元素。
 * <p>
 * 如果任何值在数组中出现至少两次，函数返回 true。如果数组中每个元素都不相同，则返回 false。
 * <p>
 * 示例 1:
 * 输入: [1,2,3,1]
 * 输出: true
 */
public class Solution4 {

    public boolean containsDuplicate(int[] nums) {
        long s1 = System.currentTimeMillis();
        boolean result1 = containsDuplicate1(nums);
        long s2 = System.currentTimeMillis();
        boolean result2 = containsDuplicate2(nums);
        long s3 = System.currentTimeMillis();
        System.out.println("第一个方法执行了("+(s2-s1)+")毫秒,结果为"+result1);
        System.out.println("第二个方法执行了("+(s3-s2)+")毫秒,结果为"+result2);
        return true;
    }

    public boolean containsDuplicate1(int[] nums) {
        int count = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                count++;
                if (nums[i] == nums[j]) {
                    System.out.println("第一个方法执行("+count+")次,找到了相同数字"+nums[i]);
                    return true;
                }
            }
        }
        System.out.println("第一个方法执行("+count+")次,没有相同数字");
        return false;
    }

    public boolean containsDuplicate2(int[] nums) {
        int count = 0;
        int[] nums_tmp = new int[nums.length+1];
        for (int i = 0; i < nums.length; i++) {
            count++;
            int index = Math.abs(nums[i]) % nums.length;
            while (nums_tmp[index] != 0) {
                count++;
                if (nums_tmp[index] == nums[i]) {
                    System.out.println("第一个方法执行("+count+")次,找到了相同数字"+(nums[i]));
                    return true;
                }
                index = (index + 1) % nums.length;
            }
            nums_tmp[index] = nums[i];
        }
        System.out.println("第二个方法执行("+count+")次,没有相同数字");
        return false;
    }

    public static void main(String[] args) {
        int[] nums = {1,2,-3,4,-6,-3,8,5,7,7,-3};
        int n = 30000;
        nums = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = 10000-i;
        }
        boolean isExist = new Solution4().containsDuplicate(nums);
        if (isExist) {
            System.out.println("存在重复数据");
        } else {
            System.out.println("不存在重复数据");
        }
    }
}
