/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Polygon2DTest {
    Polygon2D poly = null;

    static Polygon2D randPoly() {
        int sides = (int) (Math.random() * 10) + 3;
        Point2D[] ps = new Point2D[sides];
        for (int i = 0; i < sides; ++i) {
            ps[i] = Point2DTest.randPoint();
        }

        Point2D cen = ps[0];
        Comparator<Point2D> angleComp = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D p1, Point2D p2) {
                if (p1 == cen) return Integer.MAX_VALUE;
                Point2D vec1 = cen.vector(p1);
                Point2D vec2 = cen.vector(p2);
                return Double.compare(vec1.angleDegrees(), vec2.angleDegrees());
            }
        };

        //GeoShapeableTest.showShape(new Polygon2D(ps));

        Arrays.sort(ps, angleComp);

        //GeoShapeableTest.showShape(new Polygon2D(ps));

        return new Polygon2D(ps);
    }

    @BeforeEach
    void beforeEach() {
        poly = randPoly();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        GeoShapeableTest.testArea(poly);  // TODO: this fails
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testContains() {
        GeoShapeableTest.testContains(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCopy() {
        GeoShapeableTest.testCopy(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testGetPoints() {
        GeoShapeableTest.testGetPoints(poly);
        Point2D[] ps = poly.getPoints();
        Polygon2D newPoly = new Polygon2D(ps);
        assertEquals(poly, newPoly, "Polygon created with points must be equal");
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testMove() {
        GeoShapeableTest.testMove(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testPerimeter() {
        GeoShapeableTest.testPerimeter(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testRotate() {
        GeoShapeableTest.testRotate(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testScale() {
        GeoShapeableTest.testScale(poly);
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testToString() {
        GeoShapeableTest.testToString(poly);
    }
}
