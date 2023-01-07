package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Exe.Ex4.Ex4_Const;

public class Point2DTest {
    private Point2D p = null;

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
    void testStringConstructor() {
        String str = p.toString();
        assertEquals(p, new Point2D(str));
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
        // GeoShapeableTest.testArea(p);   // does not work for point
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
        GeoShapeableTest.testCopy(p);
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
    void testDistanceFromLine() {
        Segment2D seg = Segment2DTest.randSegment();
        double dist = p.distance(seg);
        assertTrue(dist >= 0, "Calculated negative distance from segment");
        Point2D[] ps = seg.getPoints();
        assertTrue(dist <= p.distance(ps[0]), "Point cannot be further closer to a point on segment than it is to the segment");
        assertTrue(dist <= p.distance(ps[1]), "Point cannot be further closer to a point on segment than it is to the segment");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testEquals() {
        GeoShapeableTest.testCopy(p);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        GeoShapeableTest.testGetPoints(p);
        Point2D[] ps = p.getPoints();
        assertEquals(1, ps.length, "Point2D.getPoints should return array of size 1");
        assertEquals(p, ps[0], "First point of Point2D.getPoints should be original point");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testIx() {
        int ix = p.ix();
        assertTrue((p.x() - ix) < 1, "Point2D.ix() should be within 1 of true x");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testIy() {
        int iy = p.iy();
        assertTrue((p.y() - iy) < 1, "Point2D.iy() should be within 1 of true y");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        Point2D origP = (Point2D) p.copy();
        Point2D vec = GeoShapeableTest.testMove(p);
        assertEquals(origP.distance(p), vec.distance(), GeoTestConsts.EPS, "Distance from original point should be equal to move vector length");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        assertEquals(0, p.perimeter(), "Point2D perimeter should be 0");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        GeoShapeableTest.testRotate(p);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotateAroundOrigin() {
        double origDist = p.distance();
        p.rotate(Math.random() * 360);
        assertEquals(origDist, p.distance(), GeoTestConsts.EPS, "Rotating around origin should not change distance from it");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        GeoShapeableTest.testScale(p);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScaleByOrigin() {
        double ratio = Math.random() * 4 - 2;   // we test negative ratio
        double origDist = p.distance();
        p.scale(ratio);
        assertEquals(Math.abs(origDist * ratio), p.distance(), GeoTestConsts.EPS, "Point distance from origin should be scaled by ratio");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        // GeoShapeableTest.testToString(p);     // Point2D's toString works in a different logic from the rest
        String[] parts = p.toString().split(",");
        assertEquals(2, parts.length, "Point2D.toString should have 3 parts");
        try {
            Double.parseDouble(parts[0]);
            Double.parseDouble(parts[1]);
        } catch (NumberFormatException e) {
            throw new AssertionError("Point2D.toString created illegal string");
        }
    }
}
