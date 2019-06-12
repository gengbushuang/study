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
        //0是没有购买
        //1为已经购买
        //
        boolean isBuy = true;
        int lastTime = 0;
        int sum = 0;
        for (int i = 0; i < prices.length; i++) {
            if (i == 0) {
                if (prices[i] < prices[i + 1]) {
                    //买入
                    lastTime = prices[i];
                    isBuy = true;
                } else {
                    isBuy = false;
                }
            } else if (i == prices.length - 1) {
                if (prices[i - 1] < prices[i]) {
                    //卖出
                    sum += prices[i] - lastTime;
                    isBuy = false;
                } else {
                    isBuy = true;
                }
            } else {
                if (prices[i - 1] > prices[i] && prices[i] < prices[i + 1]) {
                    if (!isBuy) {
                        lastTime = prices[i];
                        isBuy = true;
                    }

                } else if (prices[i - 1] < prices[i] && prices[i] > prices[i + 1]) {
                    if (isBuy) {
                        //卖出
                        sum += prices[i] - lastTime;
                        isBuy = false;
                    }
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] prices = {7,1,5,3,6,4};
        int sum = new Solution2().maxProfit(prices);
        System.out.println(sum);
    }

}
