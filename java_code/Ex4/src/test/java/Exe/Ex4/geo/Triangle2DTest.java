/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Triangle2DTest {
    Triangle2D tri;


    static Triangle2D randTri() {
        Point2D p1 = Point2DTest.randPoint(), p2 = Point2DTest.randPoint(), p3 = Point2DTest.randPoint();
        return new Triangle2D(p1, p2, p3);
    }

    @BeforeEach
    void beforeEach() {
        tri = randTri();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        GeoShapeableTest.testArea(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        GeoShapeableTest.testContains(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        GeoShapeableTest.testCopy(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        GeoShapeableTest.testGetPoints(tri);
        Point2D[] ps = tri.getPoints();
        assertEquals(tri, new Triangle2D(ps[0], ps[1], ps[2]));
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        GeoShapeableTest.testMove(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        GeoShapeableTest.testPerimeter(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        GeoShapeableTest.testRotate(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        GeoShapeableTest.testScale(tri);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        GeoShapeableTest.testToString(tri);
        String str = tri.toString();
        
        String[] splitStr = str.split(",");
        assertEquals(6, splitStr.length);
        assertEquals(tri, new Triangle2D(splitStr));
    }
}
