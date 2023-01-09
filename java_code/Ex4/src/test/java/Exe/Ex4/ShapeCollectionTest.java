/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4;

import org.junit.jupiter.api.RepeatedTest;

public class ShapeCollectionTest {
    ShapeCollection col;

    public static ShapeCollection randShapeCollection(boolean allowRotatedRect) {
        ShapeCollection ans = new ShapeCollection();
        int num = (int) (Math.random() * 17);
        for (int i = 0; i < num; ++i) {
            ans.add(GUIShapeTest.randGuiShape(allowRotatedRect));
        }
        return ans;
    }

    @RepeatedTest(TestConsts.TESTS)
    void testCopy() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testGetBoundingBox() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testLoad() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSave() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSort() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
