package algorithms.primary.array;

import java.util.*;

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
        boolean result3 = containsDuplicate3(nums);
        long s4 = System.currentTimeMillis();

        System.out.println("第一个方法执行了(" + (s2 - s1) + ")毫秒,结果为" + result1);
        System.out.println("第二个方法执行了(" + (s3 - s2) + ")毫秒,结果为" + result2);
        System.out.println("第三个方法执行了(" + (s4 - s3) + ")毫秒,结果为" + result3);
        return true;
    }

    public boolean containsDuplicate1(int[] nums) {
        int count = 0;
        int tmp;
        int ctmp;
        for (int i = 1; i < nums.length; i++) {
            count++;
            tmp = i;
            while (nums[tmp - 1] > nums[tmp]) {
                ctmp = nums[tmp];
                nums[tmp] = nums[tmp - 1];
                nums[tmp - 1] = ctmp;
                count++;
                if (tmp != 1) {
                    tmp--;
                } else {
                    break;
                }
            }
            if (nums[tmp - 1] == nums[tmp]) {
                System.out.println("第一个方法执行(" + count + ")次,找到了相同数字" + (nums[i]));
                return true;
            }
        }
        System.out.println("第一个方法执行(" + count + ")次,没有相同数字");
        return false;
    }

    public boolean containsDuplicate2(int[] nums) {
        int count = 0;
        int[] nums_tmp = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            count++;
            int index = Math.abs(nums[i]) % nums.length;
            while (nums_tmp[index] != 0) {
                count++;
                if (nums_tmp[index] == nums[i]) {
                    System.out.println("第二个方法执行(" + count + ")次,找到了相同数字" + (nums[i]));
                    return true;
                }
                index = (index + 1) % nums.length;
            }
            nums_tmp[index] = nums[i];
        }
        System.out.println("第二个方法执行(" + count + ")次,没有相同数字");
        return false;
    }

    public boolean containsDuplicate3(int[] nums) {
        Set<Integer> sets = new HashSet<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if (sets.contains(nums[i])) {
                return true;
            }
            sets.add(nums[i]);
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 1};
//        int n = 30000;
//        nums = new int[n];
//        Random r = new Random();
//        for (int i = 0; i < n; i++) {
//            nums[i] = 10000 - i;
//        }
        boolean isExist = new Solution4().containsDuplicate(nums);
        if (isExist) {
            System.out.println("存在重复数据");
        } else {
            System.out.println("不存在重复数据");
        }
    }
}
