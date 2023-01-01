/*
 * This class contains tests that are common among all GeoShapeables.
 * TODO: should this all be static?
 */

package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    }

    void testContains(GeoShapeable geo) {

    }

    void testCopy(GeoShapeable geo) {

    }

    void testGetPoints(GeoShapeable geo) {

    }

    void testMove(GeoShapeable geo) {

    }

    void testPerimeter(GeoShapeable geo) {
    }

    void testRotate(GeoShapeable geo) {

    }

    void testScale(GeoShapeable geo) {
        double ratio = Math.random();
        double oldPerimeter = geo.perimeter(), expectedNewPerimeter = oldPerimeter * ratio;
        double oldArea = geo.area(), expectedNewArea = oldArea * ratio * ratio;
        geo.scale(Point2D.ORIGIN, ratio);
        assertEquals(expectedNewArea, geo.area(), GeoTestConsts.EPS);
        assertEquals(expectedNewPerimeter, geo.perimeter(), GeoTestConsts.EPS);

    }

    void testToString(GeoShapeable geo) {
        String str = geo.toString();
        String expectedName = geo.getClass().getSimpleName();
        assertEquals(expectedName, str.split(",")[0]);
    }
}
