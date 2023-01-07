package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Circle2DTest {
    Circle2D circ = null;
    GeoShapeableTest superTest = new GeoShapeableTest();

    public static Circle2D randCircle() {
        Point2D center = Point2DTest.randPoint();
        double radius = Math.random() * 10;
        return new Circle2D(center, radius);
    }

    @BeforeEach
    void beforeEach() {
        circ = randCircle();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        superTest.testArea(circ);
        Rect2D boundingBox = GeoShapeableTest.boundingBox(circ);
        double boundingBoxArea = boundingBox.area();
        assertTrue(boundingBox.isSquare(), "Circle is bounded by non-square rectangle");
        double expectedArea = boundingBoxArea * Math.PI / 4;
        assertEquals(expectedArea, circ.area(), GeoTestConsts.EPS, "Circle area is not correctly proportional to bounding box");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        superTest.testContains(circ);
        Point2D p = Point2DTest.randPoint();
        assertTrue(circ.contains(p) == (circ.getPoints()[0].distance(p) <= circ.getRadius()), "Said contained on a point outside circle"); // make sure it is contained iff the distance is less than or equal the radius
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        superTest.testCopy(circ);
        Circle2D copy = (Circle2D) circ.copy();
        assertEquals(circ.getRadius(), copy.getRadius(), "Copy changed radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        superTest.testGetPoints(circ);

        Point2D[] ps = circ.getPoints();
        assertEquals(2, ps.length, "Circle2D.getPoints didn't return 2 points");
        assertEquals(circ.getRadius(), ps[0].distance(ps[1]), GeoTestConsts.EPS, "Circle2D.getPoints distance is not radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetRadius() {
        assertTrue(circ.getRadius() > 0, "Circle returned negative radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        double oldRad = circ.getRadius();
        superTest.testMove(circ);
        double newRad = circ.getRadius();

        assertEquals(oldRad, newRad, "Circle2D.move changed radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        superTest.testPerimeter(circ);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        double oldRad = circ.getRadius();
        superTest.testRotate(circ);
        double newRad = circ.getRadius();

        assertEquals(oldRad, newRad, "Circle2D.rotate changed radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        double oldRad = circ.getRadius();
        double ratio = superTest.testScale(circ);
        double newRad = circ.getRadius();

        assertEquals(oldRad * Math.abs(ratio), newRad, "Circle2D.scale changed radius");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        superTest.testToString(circ);
        
        String str = circ.toString();
        String[] splitStr = str.split(",");
        assertEquals(4, splitStr.length);
    }
}
