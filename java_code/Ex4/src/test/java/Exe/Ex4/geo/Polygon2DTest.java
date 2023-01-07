package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Polygon2DTest {
    Polygon2D poly = null;
    GeoShapeableTest superTest = new GeoShapeableTest();

    static Polygon2D randPoly() {
        int sides = (int) (Math.random() * 10) + 3;
        Point2D[] ps = new Point2D[sides];
        for (int i = 0; i < sides; ++i) {
            ps[i] = Point2DTest.randPoint();
        }
        return new Polygon2D(ps);
    }

    @BeforeEach
    void beforeEach() {
        poly = randPoly();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        superTest.testArea(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        superTest.testContains(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        superTest.testCopy(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        superTest.testGetPoints(poly);
        Point2D[] ps = poly.getPoints();
        Polygon2D newPoly = new Polygon2D(ps);
        assertEquals(poly, newPoly, "Polygon created with points must be equal");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        superTest.testMove(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        superTest.testPerimeter(poly);
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

    @RepeatedTest(GeoTestConsts.TESTS)
    void testTriangleMesh() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
