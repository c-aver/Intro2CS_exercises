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

import Exe.Ex4.geo.Circle2D;
import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.GeoShapeableTest;
import Exe.Ex4.geo.Polygon2D;
import Exe.Ex4.geo.Rect2D;
import Exe.Ex4.geo.Segment2D;
import Exe.Ex4.geo.Triangle2D;

public class GUIShapeTest {
    GUIShape sh;

    private static GUIShape randGuiShape() {
        GeoShapeable gs = GeoShapeableTest.randShape();
        
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
    void testString() {
        String str = sh.toString();
        String[] splitStr = str.split(",");
        assertEquals("GUIShape", splitStr[0], "First part of GUIShape.toString() should be \"GUIShape\"");
        assertEquals(sh.getColor(), new Color(Integer.parseInt(splitStr[1])), "toString() color is not correct");
        assertEquals(sh.isFilled(), Boolean.parseBoolean(splitStr[2]), "toString() fill is not correct");
        assertEquals(sh.getTag(), Integer.parseInt(splitStr[3]), "toString() tag is not correct");
        assertEquals(sh.getShape().getClass().getSimpleName(), splitStr[4]);
        String gsType = sh.getShape().getClass().getSimpleName();
        GeoShapeable gs = null;
        String[] shapeArgs = new String[splitStr.length - 5];
        System.arraycopy(splitStr, 5, shapeArgs, 0, shapeArgs.length);
        switch (gsType) {
            case "Circle2D":
                gs = new Circle2D(shapeArgs);
                break;
            case "Polygon2D":
                gs = new Polygon2D(shapeArgs);
                break;
            case "Rect2D":
                gs = new Rect2D(shapeArgs);
                break;
            case "Segment2D":
                gs = new Segment2D(shapeArgs);
                break;
            case "Triangle2D":
                gs = new Triangle2D(shapeArgs);
                break;
            default:
                assert false : "Unreachable";
        }
        assertEquals(sh.getShape(), gs);

        assertEquals(sh, new GUIShape(str));
    }
}
