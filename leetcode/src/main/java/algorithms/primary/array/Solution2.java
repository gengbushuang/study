package algorithms.primary.array;

/**
 * 买卖股票的最佳时机 II
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * <p>
 * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 * <p>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * [7,1,5,3,6,4]
 */
public class Solution2 {

    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        boolean isBuy = false;
        int sum = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) {
                if (!isBuy) {
                    //System.out.println(isBuy+"(买入),"+prices[i - 1]);
                    sum-=prices[i - 1];
                    isBuy=!isBuy;
                }
            }else{
                if(isBuy){
                    //System.out.println(isBuy+"(卖出),"+prices[i - 1]);
                    sum+=prices[i - 1];
                    isBuy=!isBuy;
                }
            }
        }
        if(isBuy){
            sum+=prices[prices.length-1];
            //System.out.println(isBuy+"(卖出),"+prices[prices.length-1]);
        }

        return sum;
    }

    public static void main(String[] args) {
        int[] prices = {7,6,4,3,1};
        int sum = new Solution2().maxProfit(prices);
        System.out.println(sum);
    }

}
