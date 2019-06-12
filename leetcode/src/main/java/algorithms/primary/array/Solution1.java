package algorithms.primary.array;

/**
 * 从排序数组中删除重复项
 * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 *
 * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
 *
 *
 * nums = [0,0,1,1,1,2,2,3,3,4],
 */
public class Solution1 {

    public int removeDuplicates(int[] nums) {
        int start = 0;
        for(int i = 1;i<nums.length;i++){
            if(nums[start]==nums[i]){
                continue;
            }
            int tmp = nums[start+1];
            nums[start+1] = nums[i];
            nums[i] = tmp;
            start+=1;
        }
        return start+1;
    }

    public static void main(String[] args) {
        int [] nums = {1,1,2};
        int len = new Solution1().removeDuplicates(nums);
        for(int i = 0;i<len;i++){
            System.out.println(nums[i]);
        }
    }
}
