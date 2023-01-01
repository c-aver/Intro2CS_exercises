package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exe.Ex4.GUIShape;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionable;

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
        ShapeCollectionable coll = new ShapeCollection();
        coll.add(new GUIShape(geo, false, null, 0));
        return randPointInBox(coll.getBoundingBox());
    }

    public static Point2D randVec() {
        return new Point2D(Math.random() * GeoTestConsts.MAX_X - (GeoTestConsts.MAX_X / 2),
                           Math.random() * GeoTestConsts.MAX_Y - (GeoTestConsts.MAX_Y / 2));
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
