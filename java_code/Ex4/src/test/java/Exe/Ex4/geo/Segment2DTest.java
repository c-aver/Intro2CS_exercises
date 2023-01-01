package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Segment2DTest {
    Segment2D seg = null;
    double length;
    GeoShapeableTest superTest = new GeoShapeableTest();

    Segment2D randSegment() {
        Point2D p1 = Point2DTest.randPoint(), p2 = Point2DTest.randPoint();
        return new Segment2D(p1, p2);
    }

    @BeforeEach
    void beforeEach() {
        seg = randSegment();
        Point2D[] ps = seg.getPoints();
        length = ps[0].distance(ps[1]);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        // superTest.testArea(seg);   // we cannot use the super test here since the definitions do not align
        assertEquals(0, seg.area(), "Claimed segment area as non-zero");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {  // TODO: this fails
        Point2D[] ps = seg.getPoints();
        Rect2D boundingBox = GeoShapeableTest.boundingBox(seg);
        Point2D p = Point2DTest.randPointInBox(boundingBox);
        double triangleLegs = p.distance(ps[0]) + p.distance(ps[1]);
        assertTrue(seg.contains(p) == (Math.abs(triangleLegs - length) < GeoTestConsts.EPS * 2));

        // TODO: implement
        // assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        superTest.testCopy(seg);
        GeoShapeable copy = seg.copy();
        assertEquals(seg.perimeter(), copy.perimeter(), "Copy changed perimeter");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        superTest.testGetPoints(seg);
        Point2D[] ps = seg.getPoints();
        assertNotNull(ps, "Segment2D.getPoints returned null");
        assertEquals(2, ps.length, "Segment2D.getPoints didn't return 2 points");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        superTest.testMove(seg);
        Point2D[] ps = seg.getPoints();
        assertEquals(length, ps[0].distance(ps[1]), GeoTestConsts.EPS, "Move changed segment length");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        superTest.testPerimeter(seg);
        assertEquals(length * 2, seg.perimeter(), "Perimeter not calculated to two times length");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        superTest.testRotate(seg);
        Point2D[] ps = seg.getPoints();
        assertEquals(length, ps[0].distance(ps[1]), GeoTestConsts.EPS, "Rotate changed segment length");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        double ratio = superTest.testScale(seg);
        Point2D[] ps = seg.getPoints();
        assertEquals(length * ratio, ps[0].distance(ps[1]), GeoTestConsts.EPS, "Rotate changed segment length");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        superTest.testToString(seg);
        String str = seg.toString();
        String[] splitStr = str.split(",");
        assertEquals(5, splitStr.length, "Segment2D.toString didn't return 5 parts");
    }
}
