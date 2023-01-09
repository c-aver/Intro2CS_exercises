/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class Polygon2DTest {
    Polygon2D poly = null;

    public static boolean isPointAbove(Point2D p, Segment2D line) {
        Point2D p1 = line.getPoints()[0], p2 = line.getPoints()[1];
        double slope = (p1.y() - p2.y()) / (p1.x() - p2.x());
        double yIntercept = p1.y() - slope * p1.x();
        double yUnderPoint = p.x() * slope + yIntercept;
        return p.y() > yUnderPoint;
    }

    public static Polygon2D randPoly() {
        // Requirement: Non-selfintersecting polygon
        // Concept: Take the two extreme points on the x-axis and draw a line between them
        // Divide all other points to points below and above the line
        // Sort one group by x values ascending and the other descending
        // Connect a line by the sorting order, using the extreme points to switch between groups
        int sides = (int) (Math.random() * 10) + 3;
        ArrayList<Point2D> ps = new ArrayList<Point2D>();
        for (int i = 0; i < sides; ++i) {
            ps.add(Point2DTest.randPoint());
        }
        Comparator<Point2D> xComp = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D p1, Point2D p2) {
                return Double.compare(p1.x(), p2.x());
            }
        };
        Point2D minP = Collections.min(ps, xComp);
        Point2D maxP = Collections.max(ps, xComp);
        Segment2D divLine = new Segment2D(minP, maxP);
        ArrayList<Point2D> above = new ArrayList<Point2D>(), below = new ArrayList<Point2D>();
        for (Point2D p : ps) {
            if (p == maxP || p == minP) continue;
            if (isPointAbove(p, divLine)) above.add(p);
            else below.add(p);
        }
        Collections.sort(below, xComp);
        Collections.sort(above, xComp);
        Collections.reverse(above);

        ArrayList<Point2D> finalPoints = new ArrayList<Point2D>();
        finalPoints.add(minP);
        finalPoints.addAll(below);
        finalPoints.add(maxP);
        finalPoints.addAll(above);

        return new Polygon2D(finalPoints.toArray(new Point2D[0]));
    }

    @BeforeEach
    void beforeEach() {
        poly = randPoly();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testArea() {
        GeoShapeableTest.testArea(poly);
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
