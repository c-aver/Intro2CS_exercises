package Exe.Ex4.geo;

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
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testContains() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testCopy() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testGetPoints() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testGetRadius() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testMove() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testPerimeter() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testRotate() {
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
