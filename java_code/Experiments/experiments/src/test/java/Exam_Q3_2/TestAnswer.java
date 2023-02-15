package Exam_Q3_2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.RepeatedTest;

public class TestAnswer {
    @RepeatedTest(1000)
    public void testAnswer() {
        BinaryTree<Integer> bt = new BinaryTree1<Integer>();
        final int size = (int) (Math.random() * 20);
        for (int i = 0; i < size; ++ i) {
            bt.add((int) (Math.random() * 100));
        }
        int[] myAns = MyAnswer.numberOfLeafsByLevel(bt);
        int[] publishedAns = PublishedAnswer.numberOfLeafsByLevel(bt);
        assertArrayEquals(publishedAns, myAns);
    }
}
