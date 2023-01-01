/*
 * This class contains tests that are common among all GeoShapeables.
 * TODO: should this all be static?
 */

package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GeoShapeableTest {
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
        assertTrue(area > 0, "Area returned non-positive value"); // TODO: >= ?
    }

    void testContains(GeoShapeable geo) {
        // TODO: implement
        assert false : "Not implemented";
    }

    void testCopy(GeoShapeable geo) {
        // TODO: implement
        assert false : "Not implemented";
    }

    void testGetPoints(GeoShapeable geo) {
        Point2D[] ps = geo.getPoints();
        assertNotNull(ps, "getPoints returned null");
        assertTrue(ps.length > 0, "getPoints returned empty array");
    }

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

        assertEquals(expectedPs, geo.getPoints(), "Points moved unexpectedly");
        
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

        assertEquals(ps, geo.getPoints(), "Points moved unexpectedly");

        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Rotate changed area");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Rotate changed perimeter");
    }

    void testScale(GeoShapeable geo) {
        double ratio = Math.random();
        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter * ratio;
        double oldArea = geo.area(), expectedNewArea = oldArea * ratio * ratio;
        geo.scale(Point2D.ORIGIN, ratio);
        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS, "Scaled area incorrectly");
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS, "Scaled perimeter incorrectly");

    }

    void testToString(GeoShapeable geo) {
        String str = geo.toString();
        String expectedName = geo.getClass().getSimpleName();
        assertEquals(expectedName, str.split(",")[0], "toString first value is not class name");
    }
}
