package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Point2DTest {
    private Point2D p = null;

    public static Point2D randPoint() {
        return new Point2D(Math.random() * GeoTestConsts.MAX_X, Math.random() * GeoTestConsts.MAX_Y);
    }

    @BeforeEach
    void beforeEach() {
        p = randPoint();
    }

    @Test
    void testAdd() {
        Point2D addP = randPoint();
        Point2D newP = p.add(addP);
        assertEquals(p.x() + addP.x(), newP.x(), GeoTestConsts.EPS);
        assertEquals(p.y() + addP.y(), newP.y(), GeoTestConsts.EPS);
    }

    @Test
    void testAngleDegrees() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testArea() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testCloseToEquals() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testCloseToEquals2() {
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
    void testDistance() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testDistance2() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testDistance3() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testEquals() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testGetPoints() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testIx() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testIy() {
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
    void testRotate2() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testScale() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testScale2() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testVector() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testX() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @Test
    void testY() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
