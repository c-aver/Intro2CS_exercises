/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class ShapeCollectionTest {
    ShapeCollection col;

    public static ShapeCollection randShapeCollection(boolean allowRotatedRect) {
        ShapeCollection ans = new ShapeCollection();
        int num = (int) (Math.random() * 17);
        for (int i = 0; i < num; ++i) {
            GUIShape sh = GUIShapeTest.randGuiShape(allowRotatedRect);
            sh.setTag(i + 1);
            ans.add(sh);
        }
        return ans;
    }

    @BeforeEach
    void beforeEach() {
        col = randShapeCollection(true);
    }

    @RepeatedTest(TestConsts.TESTS)
    void testCopy() {
        ShapeCollection copy = (ShapeCollection) col.copy();
        assertNotEquals(System.identityHashCode(col), System.identityHashCode(copy), "ShapeCollection copy is shallow");
        for (int i = 0; i < copy.size(); ++i) {
            assertEquals(col.get(i), copy.get(i), "ShapeCollection copy elements should be (logically) equal");
            assertNotEquals(System.identityHashCode(col.get(i)), System.identityHashCode(copy.get(i)), "ShapeCollection copy element is shallowly copied");
        }
    }

    // @RepeatedTest(TestConsts.TESTS)
    void testGetBoundingBox() {
        // TODO: implement
        assert false : "Not implemented";
    }

    // @RepeatedTest(TestConsts.TESTS)
    void testLoad() {
        // TODO: implement
        assert false : "Not implemented";
    }

    // @RepeatedTest(TestConsts.TESTS)
    void testSave() {
        // TODO: implement
        assert false : "Not implemented";
    }

    // @RepeatedTest(TestConsts.TESTS)
    void testSort() {
        // TODO: implement
        assert false : "Not implemented";
    }

    // @RepeatedTest(TestConsts.TESTS)
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
