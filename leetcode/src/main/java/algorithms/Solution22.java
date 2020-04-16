package algorithms;

class Solution22 {
    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public void test() {
        ListNode a1 = new ListNode(1);
        a1.next = new ListNode(8);
//        a1.next.next = new ListNode(4);

        ListNode b1 = new ListNode(0);
//        b1.next = new ListNode(9);
//        b1.next.next = new ListNode(5);

        ListNode listNode = addTwoNumbers(a1, b1);
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return addTwoNumbers2(l1, l2, 0);
    }

    private ListNode addTwoNumbers2(ListNode l1, ListNode l2, int tmp) {
        if (l1 != null || l2 != null) {
            int result = (l1==null?0:l1.val) + (l2==null?0:l2.val) + tmp;
            if (result > 9) {
                tmp = result / 10;
                result = result % 10;
            } else {
                tmp = 0;
            }
            ListNode resultNode = new ListNode(result);
            resultNode.next = addTwoNumbers2(l1==null?null:l1.next, l2==null?null:l2.next, tmp);
            return resultNode;
        }
        if (tmp > 0) {
            return new ListNode(tmp);
        }
        return null;
    }

    public static void main(String[] args) {
        new Solution22().test();

    }
}