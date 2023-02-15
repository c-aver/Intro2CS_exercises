package Exam_Q3_2;

public class MyAnswer {
    public static int[] numberOfLeafsByLevel(BinaryTree bt) {
        if (bt == null || bt.isEmpty()) return null;
        BinaryTree l = bt.getLeft(), r = bt.getRight();
        if (l == null && r == null) { // root is leaf
            int[] ans = new int[1];
            ans[0] = 1;
            return ans;
        }
        int[] lLeafs = numberOfLeafsByLevel(l), rLeafs = numberOfLeafsByLevel(r);
        if (lLeafs == null) lLeafs = new int[0];
        if (rLeafs == null) rLeafs = new int[0];
        int lLevs = lLeafs.length, rLevs = rLeafs.length, levs = 1 + Math.max(lLevs, rLevs);
        int[] ans = new int[levs];
        ans[0] = 0;
        for (int i = 1; i < levs; ++i) {
            ans[i] = (i - 1 < lLevs ? lLeafs[i-1] : 0)
                   + (i - 1 < rLevs ? rLeafs[i-1] : 0);
        }
        return ans;
    }
}
