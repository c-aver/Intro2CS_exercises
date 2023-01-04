/*
 * This class contains tests that are common among all GeoShapeables.
 * TODO: should this all be static?
 */

package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Exe.Ex4.GUIShape;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionable;


public class GeoShapeableTest {
    static Rect2D boundingBox(GeoShapeable geo) {
        ShapeCollectionable coll = new ShapeCollection();
        coll.add(new GUIShape(geo, false, null, 0));
        return coll.getBoundingBox();
    }

    // this is just in case I need it, should not use
    void runAll(GeoShapeable geo) {
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

    void testArea(GeoShapeable geo) {
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
        assertEquals(areaRatio, hitRate, GeoTestConsts.EPS * 10, "Dart hit rate does not match area ratio");
    }

    void testContains(GeoShapeable geo) {
        Rect2D boundingBox = boundingBox(geo);
        Point2D p = Point2DTest.randPoint();
        assertTrue(!geo.contains(p) || boundingBox.contains(p), "Point claimed outside boundingBox but inside shape");
    }

    void testCopy(GeoShapeable geo) {
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

    void testGetPoints(GeoShapeable geo) {
        Point2D[] ps = geo.getPoints();
        assertNotNull(ps, "getPoints returned null");
        assertTrue(ps.length > 0, "getPoints returned empty array");
    }

    /* Common move tests:
     * Area and perimeter do not change.
     * All points change by the move vector.
     */
    void testMove(GeoShapeable geo) {
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
    }

    void testPerimeter(GeoShapeable geo) {
        double perimeter = geo.perimeter();
        assertTrue(perimeter > 0, "Perimeter returned non-positive value");
    }

    void testRotate(GeoShapeable geo) {
        Point2D center = Point2DTest.randPoint();
        double rotateAngleDegrees = Math.random() * 360;
        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter;
        double oldArea = geo.area(), expectedNewArea = oldArea;
        Point2D[] ps = geo.getPoints();
        for (int i = 0; i < ps.length; ++i) {
            ps[i].rotate(center, rotateAngleDegrees);
        }

        geo.rotate(center, rotateAngleDegrees);

        assertEquals(ps[0], geo.getPoints()[0], "Center moved unexpectedly");

        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Rotate changed area");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Rotate changed perimeter");
    }

    double testScale(GeoShapeable geo) {
        double ratio = Math.random();
        Point2D center = Point2DTest.randPoint();

        boolean wasContained = geo.contains(center);

        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter * ratio;
        double oldArea = geo.area(), expectedNewArea = oldArea * ratio * ratio;  // due to the square-cube law we expect area to be scaled by the square

        geo.scale(center, ratio);
        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Scaled area incorrectly");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Scaled perimeter incorrectly");
        assertEquals(wasContained, geo.contains(center), "Center containment changed after scale");
        return ratio;
    }

    void testToString(GeoShapeable geo) {
        String str = geo.toString();
        String expectedName = geo.getClass().getSimpleName();
        assertNotNull(str, "toString returned null string");
        String[] splitStr = str.split(",");
        assertTrue(splitStr.length > 2, "toString returned too few commas");
        assertEquals(expectedName, splitStr[0], "toString first value is not class name");
    }
}

// TODO: test string constructors
