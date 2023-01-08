/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Exe.Ex4.geo.Circle2DTest;
import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.Point2DTest;
import Exe.Ex4.geo.Polygon2DTest;
import Exe.Ex4.geo.Rect2DTest;
import Exe.Ex4.geo.Triangle2DTest;

public class GUIShapeTest {
    GUIShape sh;

    private static GUIShape randGuiShape() {
        GeoShapeable gs = null;
        int type = (int) (Math.random() * 6);
        switch (type) {
            case 0:
                gs = Circle2DTest.randCircle();
                break;
            case 1:
                gs = Point2DTest.randPoint();
                break;
            case 2:
                gs = Polygon2DTest.randPoly();
                break;
            case 3:
                gs = Circle2DTest.randCircle();
                break;
            case 4:
                gs = Rect2DTest.randRect();
                break;
            case 5:
                gs = Triangle2DTest.randTri();
                break;
            default:
                assert false : "Unreachable";
        }
        boolean fill = Math.random() < 0.5;
        Color c = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        return new GUIShape(gs, fill, c, 0);
    }

    @BeforeEach
    void beforeEach() {
        sh = randGuiShape();
    }

    @RepeatedTest(TestConsts.TESTS)
    void testCopy() {
        GUI_Shapeable copy = sh.copy();
        assertNotEquals(System.identityHashCode(sh), System.identityHashCode(copy), "copy returned shallow copy");
        assertEquals(sh.isFilled(), copy.isFilled(), "Copy changed fill state");
        assertEquals(sh.isSelected(), copy.isSelected(), "Copy changed select state");
        assertEquals(sh.getColor(), copy.getColor(), "Copy changed color");

        assertNotEquals(System.identityHashCode(sh.getShape()), System.identityHashCode(copy.getShape()), "Copied GeoShapeable shallowly");
    }

    @RepeatedTest(TestConsts.TESTS)
    void testGetColor() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testGetShape() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testGetTag() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testIsFilled() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testIsSelected() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSetColor() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSetFilled() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSetSelected() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSetShape() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSetTag() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(TestConsts.TESTS)
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
