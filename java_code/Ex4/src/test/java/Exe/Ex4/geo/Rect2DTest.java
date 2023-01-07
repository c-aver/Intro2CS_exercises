package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Rect2DTest {
    Rect2D rect;

    static Rect2D randRect() {
        Point2D p1 = Point2DTest.randPoint();
        Point2D p2 = Point2DTest.randPoint();
        Rect2D rect = new Rect2D(p1, p2);
        if (Math.random() < 0.5) {      // a certain chance for the rectangle to be not axis parallel
            double angleDegrees = Math.random() * 360;
            Point2D cen = Point2DTest.randPoint();
            rect.rotate(cen, angleDegrees);
        }
        return rect;
    }

    @BeforeEach
    void beforeEach() {
        rect = randRect();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testString() {
        String str = rect.toString();
        assertEquals(rect, new Rect2D(str.split(",")));  // TODO: nulls?
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        GeoShapeableTest.testArea(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        GeoShapeableTest.testContains(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        GeoShapeableTest.testCopy(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        GeoShapeableTest.testGetPoints(rect);
        Point2D[] ps = rect.getPoints();
        Rect2D boundingBox = GeoShapeableTest.boundingBox(rect);
        assertEquals(boundingBox, new Rect2D(ps[0], ps[1]));
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        GeoShapeableTest.testMove(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        GeoShapeableTest.testPerimeter(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        GeoShapeableTest.testRotate(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        GeoShapeableTest.testScale(rect);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        GeoShapeableTest.testToString(rect);
    }
}
