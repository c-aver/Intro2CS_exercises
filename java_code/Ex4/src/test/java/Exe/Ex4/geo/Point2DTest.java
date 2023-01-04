package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Exe.Ex4.Ex4_Const;

public class Point2DTest {
    private Point2D p = null;
    GeoShapeableTest superTest = new GeoShapeableTest();

    public static Point2D randPoint() {
        return new Point2D(Math.random() * GeoTestConsts.MAX_X, Math.random() * GeoTestConsts.MAX_Y);
    }
    public static Point2D randPointInBox(Rect2D box) {
        Point2D[] ps = box.getPoints();
        double minX = ps[0].x(), maxX = ps[1].x(), minY = ps[0].y(), maxY = ps[1].y();
        double dx = maxX - minX, dy = maxY - minY;
        double x = Math.random() * dx + minX, y = Math.random() * dy + minY;
        return new Point2D(x, y);
    }
    public static Point2D randPointInBoundingBox(GeoShapeable geo) {
        return randPointInBox(GeoShapeableTest.boundingBox(geo));
    }

    public static Point2D randVec() {
        return new Point2D(Math.random() * GeoTestConsts.MAX_X - (GeoTestConsts.MAX_X / 2),
                           Math.random() * GeoTestConsts.MAX_Y - (GeoTestConsts.MAX_Y / 2));
    }

    @BeforeEach
    void beforeEach() {
        p = randPoint();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testAdd() {
        Point2D addP = randPoint();
        Point2D newP = p.add(addP);
        assertEquals(p.x() + addP.x(), newP.x(), GeoTestConsts.EPS);
        assertEquals(p.y() + addP.y(), newP.y(), GeoTestConsts.EPS);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testAngleDegrees() {
        double angle = p.angleDegrees();
        assertTrue(angle >= 0, "Calculated negative angle");
        assertTrue(angle < 360, "Calculated angle greater than 360 degrees");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        // superTest.testArea(p);   // does not work for point
        assertEquals(0.0, p.area(), "Calculated area different from 0 for point");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCloseToEquals() {
        Point2D copy = (Point2D) p.copy();
        copy.move(new Point2D(Ex4_Const.EPS / 2, Ex4_Const.EPS / 2));
        assertTrue(p.closeToEquals(copy), "Moved point a little and was not close to equal");
        copy.move(new Point2D(Ex4_Const.EPS / 2, Ex4_Const.EPS / 2));
        assertFalse(p.closeToEquals(copy), "Moved point a lot and was close to equal");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCloseToEquals2() {
        Point2D copy = (Point2D) p.copy();
        double eps = Math.random();
        copy.move(new Point2D(eps / 2, eps / 2));
        assertTrue(p.closeToEquals(copy, eps));
        copy.move(new Point2D(eps / 2, eps / 2));
        assertFalse(p.closeToEquals(copy), "Moved point a lot and was close to equal");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        Point2D can = randPoint();
        assertEquals(p.equals(can), p.contains(can), "Point needs to contain iff they are equal");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        superTest.testCopy(p);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testDistanceFromOrigin() {
        double expectedDist = Math.sqrt(p.x()*p.x() + p.y()*p.y());
        assertEquals(expectedDist, p.distance(), GeoTestConsts.EPS, "Distance from origin not as expected by Pythagoras");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testDistance() {
        Point2D copy = (Point2D) p.copy();
        double moveX = Math.random() * GeoTestConsts.MAX_X;
        double moveY = Math.random() * GeoTestConsts.MAX_Y;
        copy.move(new Point2D(moveX, 0));
        assertEquals(moveX, p.distance(copy), GeoTestConsts.EPS, "Point moved and got incorrect distance");
        copy.move(new Point2D(0, moveY));
        double expectedDist = Math.sqrt(moveX*moveX + moveY*moveY);  // expected total distance according to Pythagoras
        assertEquals(expectedDist, p.distance(copy), GeoTestConsts.EPS, "Point moved and got incorrect distance");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testDistance3() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testEquals() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testIx() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testIy() {
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
    void testRotate2() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale2() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testVector() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testX() {
        // TODO: implement
        assert false : "Not implemented";
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testY() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
