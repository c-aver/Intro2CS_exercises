/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

/*
 * This library contains tests that are common among all GeoShapeables.
 */

package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Exe.Ex4.GUIShape;
import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionable;
import Exe.Ex4.gui.Ex4;

import java.awt.Color;

public class GeoShapeableTest {
    static Rect2D boundingBox(GeoShapeable geo) {
        ShapeCollectionable coll = new ShapeCollection();
        coll.add(new GUIShape(geo, false, null, 0));
        return coll.getBoundingBox();
    }

    // this is just in case I need it, should not use
    static void runAll(GeoShapeable geo) {
        testArea(geo);
        testContains(geo);
        testCopy(geo);
        testGetPoints(geo);
        testMove(geo);
        testPerimeter(geo);
        testRotate(geo);
        testScale(geo);
        testToString(geo);
    }
    // This function uses the Ex4 class to show a shape on the screen
    // This is very useful for debugging specific shapes that cause tests to fail
    static void showShape(GeoShapeable geo) {
        Ex4 ex4 = Ex4.getInstance();
        ex4.getShape_Collection().removeAll();
		ShapeCollectionable shapes = ex4.getShape_Collection();
		GUI_Shapeable gs1 = new GUIShape(geo, true, Color.black, 1);
		shapes.add(gs1);
		ex4.init(shapes);
		ex4.show();
		System.out.print(ex4.getInfo());
    }

    static void testArea(GeoShapeable geo) {
        // this test takes a probabilistic method
        // basically, the area of a shape should determine the chances of hitting in with a randomally thrown dart
        // this assume that contains() and area() agree on their definitions
        double area = geo.area();
        assertTrue(area >= 0, "Area returned non-positive value");
        Rect2D boundingBox = boundingBox(geo);
        assertTrue(geo.area() <= boundingBox.area(), "Calculated area larger than bounding box");
        int hits = 0;
        for (int i = 0; i < GeoTestConsts.DARTS; ++i) {
            Point2D dart = Point2DTest.randPointInBox(boundingBox);
            if (geo.contains(dart)) hits += 1;
        }
        double hitRate = ((double) hits) / GeoTestConsts.DARTS;
        double areaRatio = geo.area() / boundingBox.area();
        assertEquals(areaRatio, hitRate, 0.05, "Dart hit rate does not match area ratio");  // there is a tradeoff between the allowed delta and number of darts (which adds to run time)
    }

    static void testContains(GeoShapeable geo) { // note that contains() is also mostly tested in testArea()
        Rect2D boundingBox = boundingBox(geo);
        Point2D p = Point2DTest.randPoint();
        assertTrue(!geo.contains(p) || boundingBox.contains(p), "Point claimed outside boundingBox but inside shape");
    }

    static void testCopy(GeoShapeable geo) {
        GeoShapeable copy = geo.copy();
        assertNotEquals(System.identityHashCode(geo), System.identityHashCode(copy), "copy returned shallow copy");
        Point2D[] ps = geo.getPoints(), copyPs = copy.getPoints();
        assertEquals(ps.length, copyPs.length, "copy changed number of points");
        assertEquals(geo.area(), copy.area(), "Copy changed area");
        assertEquals(geo.perimeter(), copy.perimeter(), "Copy changed perimeter");
        for (int i = 0; i < ps.length; ++i) {
            assertNotEquals(System.identityHashCode(ps[i]), System.identityHashCode(copyPs[i]), "copy returned shallow point copy");
        }
    }

    static void testGetPoints(GeoShapeable geo) {
        Point2D[] ps = geo.getPoints();
        assertNotNull(ps, "getPoints returned null");
        assertTrue(ps.length > 0, "getPoints returned empty array");
    }

    /* Common move tests:
     * Area and perimeter do not change.
     * All points change by the move vector.
     */
    static Point2D testMove(GeoShapeable geo) {
        Point2D vec = Point2DTest.randVec();
        Point2D[] ps = geo.getPoints();
        
        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter;
        double oldArea = geo.area(), expectedNewArea = oldArea;

        Point2D[] expectedPs = new Point2D[ps.length];
        for (int i = 0; i < expectedPs.length; ++i) {
            expectedPs[i] = ps[i].add(vec);
        }

        geo.move(vec);
        ps = geo.getPoints();

        for (int i = 0; i < ps.length; ++i) {
            assertTrue(ps[i].closeToEquals(expectedPs[i]), "Points moved unexpectedly");
        }
        //assertEquals(expectedPs, geo.getPoints());
        
        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Move changed area");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Move changed perimeter");

        return vec;
    }

    static void testPerimeter(GeoShapeable geo) {
        double perimeter = geo.perimeter();
        assertTrue(perimeter > 0, "Perimeter returned non-positive value");
    }

    static void testRotate(GeoShapeable geo) {
        Point2D center = Point2DTest.randPoint();
        double rotateAngleDegrees = Math.random() * 360;
        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter;
        double oldArea = geo.area(), expectedNewArea = oldArea;
        Point2D[] ps = geo.copy().getPoints();
        if (geo instanceof Rect2D) ps = ((Rect2D) geo).getAllPoints();
        double[] dists = new double[ps.length];
        for (int i = 0; i < dists.length; ++i) {
            dists[i] = ps[i].distance(center);
        }
        for (int i = 0; i < ps.length; ++i) {
            ps[i].rotate(center, rotateAngleDegrees);
        }

        geo.rotate(center, rotateAngleDegrees);

        Point2D[] newPs = geo.getPoints();
        if (geo instanceof Rect2D) newPs = ((Rect2D) geo).getAllPoints();
        if (!(geo instanceof Circle2D))    // Circle2D getPoints behaves a bit differently so this should not be tested
            for (int i = 0; i < dists.length; ++i) {
                assertEquals(dists[i], newPs[i].distance(center), GeoTestConsts.EPS, "Point distance from center should not change on rotate");
            }

        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Rotate changed area");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Rotate changed perimeter");
    }

    static double testScale(GeoShapeable geo) {
        double ratio = Math.random() * 4 - 2;     // we allow negative ratios despite not being strictly required
        double absRatio = Math.abs(ratio);
        Point2D center = Point2DTest.randPoint();

        boolean wasContained = geo.contains(center);

        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter * absRatio;
        double oldArea = geo.area(), expectedNewArea = oldArea * absRatio * absRatio;  // due to the square-cube law we expect area to be scaled by the square

        Point2D[] ps = geo.copy().getPoints();
        if (geo instanceof Rect2D) ps = ((Rect2D) geo).getAllPoints();
        double[] dists = new double[ps.length];
        for (int i = 0; i < dists.length; ++i) {
            dists[i] = ps[i].distance(center);
        }

        geo.scale(center, ratio);

        Point2D[] newPs = geo.getPoints();
        if (geo instanceof Rect2D) newPs = ((Rect2D) geo).getAllPoints();
        if (!(geo instanceof Circle2D))    // Circle2D getPoints behaves a bit differently so this should not be tested
            for (int i = 0; i < dists.length; ++i) {
                assertEquals(dists[i] * absRatio, newPs[i].distance(center), GeoTestConsts.EPS, "Point distance from center should be scaled by the ratio");
            }

        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Scaled area incorrectly");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Scaled perimeter incorrectly");
        if (!(geo instanceof Segment2D))  // Segment2D containment is not very stricly defined
            assertEquals(wasContained, geo.contains(center), "Center containment changed after scale");
        return ratio;
    }

    static void testToString(GeoShapeable geo) {
        String str = geo.toString();
        assertNotNull(str, "toString returned null string");
        String[] splitStr = str.split(",");
        assertTrue(splitStr.length > 2, "toString returned too few commas");
    }

    void testEquals(GeoShapeable geo) {
        GeoShapeable copy = geo.copy();
        assertEquals(geo, copy, "Shape should be equal to its copy"); // this uses the overloaded equals method and thus tests it
    }

    public static GeoShapeable randShape() {
        int type = (int) (Math.random() * 5);
        switch (type) {
            case 0:
                return Circle2DTest.randCircle();
            case 1:
                return Polygon2DTest.randPoly();
            case 2:
                return Circle2DTest.randCircle();
            case 3:
                return Rect2DTest.randRect();
            case 4:
                return Triangle2DTest.randTri();
            default:
                assert false : "Unreachable";
        }
        return null;
    }
}
