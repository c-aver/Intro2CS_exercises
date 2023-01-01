package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Segment2DTest {
    Segment2D seg = null;
    GeoShapeableTest superTest = new GeoShapeableTest();

    Segment2D randSegment() {
        Point2D p1 = Point2DTest.randPoint(), p2 = Point2DTest.randPoint();
        return new Segment2D(p1, p2);
    }

    @BeforeEach
    void beforeEach() {
        seg = randSegment();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        // superTest.testArea(seg);
        assertEquals(0, seg.area(), "Claimed segment area as non-zero");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        Point2D[] ps = seg.getPoints();
        double length = ps[0].distance(ps[1]);
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
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
