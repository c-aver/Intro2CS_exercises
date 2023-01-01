package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void testArea() {
        superTest.testArea(circ);
        Rect2D boundingBox = GeoShapeableTest.boundingBox(circ);
        double boundingBoxArea = boundingBox.area();
        assertTrue(boundingBox.isSquare(), "Circle is bounded by non-square rectangle");
        double expectedArea = boundingBoxArea * Math.PI / 4;
        assertEquals(expectedArea, circ.area(), GeoTestConsts.EPS, "Circle area is not correctly proportional to bounding box");
        //assert false : "Not implemented";
    }

    @Test
    void testContains() {
        superTest.testContains(circ);
        assert false : "Not implemented";
    }

    @Test
    void testCopy() {
        superTest.testCopy(circ);
        Circle2D copy = (Circle2D) circ.copy();
        assertEquals(circ.getRadius(), copy.getRadius(), "Copy changed radius");
    }

    @Test
    void testGetPoints() {
        superTest.testGetPoints(circ);

        Point2D[] ps = circ.getPoints();
        assertEquals(2, ps.length, "Circle2D.getPoints didn't return 2 points");
        assertEquals(circ.getRadius(), ps[0].distance(ps[1]), GeoTestConsts.EPS, "Circle2D.getPoints distance is not radius");
    }

    @Test
    void testGetRadius() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testMove() {
        superTest.testMove(circ);

        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testPerimeter() {
        superTest.testPerimeter(circ);

        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testRotate() {
        superTest.testRotate(circ);

        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testScale() {
        superTest.testScale(circ);

        // TODO: implement
        //assert false : "Not implemented";
    }

    @Test
    void testToString() {
        superTest.testToString(circ);
        
        // TODO: implement
        //assert false : "Not implemented";
    }
}
