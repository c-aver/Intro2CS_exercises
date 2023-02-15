package Exam_Q3_2;

public class PublishedAnswer {
    public static int[] numberOfLeafsByLevel(BinaryTree bt) {
        int[] ans = null;
        if(bt!=null && bt.size()>0) {
            ans = new int[height(bt) + 1];  // fixed from ans = new int[bt.height()]
        }
        numberOfLeafsByLevel(bt, ans, 0);
        return ans;
    }

    private static void numberOfLeafsByLevel(BinaryTree bt, int[] ans, int d) {
        if(bt!=null && bt.size()>0) {
            if(bt.size() == 1) { ans[d]++; }
            else {
                numberOfLeafsByLevel(bt.getLeft(), ans, d+1);
                numberOfLeafsByLevel(bt.getRight(), ans, d+1);
            }
        }
    }

    ///////////////////////////////////////
    private static int height(BinaryTree bt) {
        int ans = -1;
        if(bt!=null && !bt.isEmpty()) {
            ans = 1 + Math.max(height(bt.getLeft()), height(bt.getRight()));
        }
        return ans;
    }
}
