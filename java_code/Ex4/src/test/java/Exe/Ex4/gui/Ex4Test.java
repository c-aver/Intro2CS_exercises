/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4.gui;

import Exe.Ex4.GUIShapeTest;
import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionTest;
import Exe.Ex4.ShapeCollectionable;
import Exe.Ex4.TestConsts;

import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Point2DTest;
import Exe.Ex4.geo.Polygon2D;
import Exe.Ex4.geo.ShapeComp;
import Exe.Ex4.geo.Triangle2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.awt.Color;
import java.util.Comparator;

/**
 * Note: this class does not test the specific methods in Ex4, instead it tests functionalities.
 * This is because the methods are very interconnected, and it is somewhat weird to test only mouseMoved, for example.
 */
public class Ex4Test {
    Ex4 ex4;

    private void moveMouseTo(Point2D p) {
        ex4.mouseMoved(null, p);   // call mouseMoved but with override p
    }

    private void randInit() {
        ex4 = Ex4.getInstance();
        ShapeCollection col = ShapeCollectionTest.randShapeCollection(true);
        for (int i = 0; i < col.size(); ++i) {
            col.get(i).setSelected(Math.random() < 0.5);
        }
        ex4.init(col);
    }

    @BeforeEach
    void beforeEach() {
        randInit();
    }

    @RepeatedTest(TestConsts.TESTS)
    void testClear() {
        ex4.actionPerformed("Clear");
        assertEquals(0, ex4.getShape_Collection().size(), "Shape collection not empty after clear");
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSelectPoint() {
        ex4.actionPerformed("Point");
        boolean[] wasSelected = new boolean[ex4.getShape_Collection().size()];
        for (int i = 0; i < wasSelected.length; ++i) {
            wasSelected[i] = ex4.getShape_Collection().get(i).isSelected();
        }
        Point2D click = Point2DTest.randPoint();
        ex4.mouseClicked(click);
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            if (sh.getShape().contains(click)) {
                assertNotEquals(wasSelected[i], sh.isSelected(), "Clicked shape selection was not reversed");
            }
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSelectAll() {
        ex4.actionPerformed("All");
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            assertTrue(sh.isSelected(), "Shape is not selected after select all");
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSelectAnti() {
        boolean[] wasSelected = new boolean[ex4.getShape_Collection().size()];
        for (int i = 0; i < wasSelected.length; ++i) {
            wasSelected[i] = ex4.getShape_Collection().get(i).isSelected();
        }
        ex4.actionPerformed("Anti");
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            assertNotEquals(wasSelected[i], sh.isSelected(), "Clicked shape selection was not reversed");
        }
    }
    
    @RepeatedTest(TestConsts.TESTS)
    void testSelectNone() {
        ex4.actionPerformed("None");
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            assertFalse(sh.isSelected(), "Shape is selected after select none");
        }
    }

    private static String colorName(Color c) {
        if (c.equals(Color.BLUE))   return "Blue"  ;
		if (c.equals(Color.RED))    return "Red"   ;
		if (c.equals(Color.GREEN))  return "Green" ;
		if (c.equals(Color.WHITE))  return "White" ;
		if (c.equals(Color.BLACK))  return "Black" ;
		if (c.equals(Color.YELLOW)) return "Yellow";
        assert false : "Illegal Color provided";
        return null;
    }

    void setShape(Class<? extends GeoShapeable> type) {
        String className = type.getSimpleName();
        String action = className.substring(0, className.length() - 2);  // cut the "2D" from the end of the name
        ex4.actionPerformed(action);
    }

    @RepeatedTest(TestConsts.TESTS)
    void testDrawShape() {
        GUI_Shapeable sh = GUIShapeTest.randGuiShape(false);  // we do not allow a rotated rectangle since it cannot be drawn (directly)
        sh.setTag(ex4.getShape_Collection().size() + 1);  // this should be the next tag
        sh.setColor(setRandColor());   // since randGuiShape gives illegal colors as well, we set the shape's color according to the passed color (this is not cheating)
        setFill(sh.isFilled());
        setShape(sh.getShape().getClass());
        for (Point2D p : sh.getShape().getPoints()) {
            moveMouseTo(p);
            ex4.mouseClicked(p);
        }
        if (sh.getShape() instanceof Polygon2D) ex4.mouseRightClicked(new Point2D(0, 0));
        GUI_Shapeable expectedShape = sh.copy();
        if (sh.getShape() instanceof Polygon2D && sh.getShape().getPoints().length == 3) {   // in this case we actually expect a triangle
            Point2D[] ps = sh.getShape().getPoints();
            expectedShape.setShape(new Triangle2D(ps[0], ps[1], ps[2]));
        }
        assertEquals(expectedShape, ex4.getShape_Collection().get(ex4.getShape_Collection().size() - 1), "Drawn shape is not the last in the colllection");
    }

    void setColor(Color c) {
        
        String cName = colorName(c);
        ex4.actionPerformed(cName);
    }

    Color setRandColor() {
        Color[] allowedColors = new Color[] {Color.BLUE, Color.RED, Color.GREEN, Color.WHITE, Color.BLACK, Color.YELLOW};
        
        Color randColor = allowedColors[(int) (Math.random() * allowedColors.length)];
        setColor(randColor);
        return randColor;
    }

    @RepeatedTest(TestConsts.TESTS)
    void testColors() {
        Color col = setRandColor();
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            if (sh.isSelected()) {
                assertEquals(col, sh.getColor(), "Selected shape not colored");
            }
        }
    }

    void setFill(boolean fill) {
        String randFillCommand = (fill ? "Fill" : "Empty");
        ex4.actionPerformed(randFillCommand);
    }

    boolean setRandFill() {
        boolean fill = Math.random() < 0.5;
        setFill(fill);
        return fill;
    }

    @RepeatedTest(TestConsts.TESTS)
    void testFill() {
        boolean fill = setRandFill();
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            GUI_Shapeable sh = ex4.getShape_Collection().get(i);
            if (sh.isSelected()) {
                assertEquals(fill, sh.isFilled(), "Selected shape not properly filled");
            }
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSorts() {   // note: this only tests that ex4 sorts the shapes, it does not test the comparators
        String[] allowedSorts = new String[] { "Area", "Perimeter", "ToString", "Tag" };
        String randSort = allowedSorts[(int) (Math.random() * allowedSorts.length)];
        boolean anti = Math.random() < 0.5;
        String action = (anti ? "ByAnti" : "By") + randSort;
        ex4.actionPerformed(action);
        ShapeCollectionable col = ex4.getShape_Collection();
        Comparator<GUI_Shapeable> expectedComp = null;
        switch (action) {
            case "ByArea": expectedComp = ShapeComp.CompByArea;                   break;
            case "ByPerimeter": expectedComp = ShapeComp.CompByPerimeter;         break;
            case "ByToString": expectedComp = ShapeComp.CompByToString;           break;
            case "ByTag": expectedComp = ShapeComp.CompByTag;                     break;
            case "ByAntiArea": expectedComp = ShapeComp.CompByAntiArea;           break;
            case "ByAntiPerimeter": expectedComp = ShapeComp.CompByAntiPerimeter; break;
            case "ByAntiToString": expectedComp = ShapeComp.CompByAntiToString;   break;
            case "ByAntiTag": expectedComp = ShapeComp.CompByAntiTag;             break;
        }
        for (int i = 0; i < col.size() - 1; ++i) {
            assertTrue(expectedComp.compare(col.get(i), col.get(i + 1)) < 0);
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testRemove() {
        int numSelected = 0;
        int numTotal = ex4.getShape_Collection().size();
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            if (ex4.getShape_Collection().get(i).isSelected()) {
                numSelected += 1;
            }
        } 
        ex4.actionPerformed("Remove");
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            assertFalse(ex4.getShape_Collection().get(i).isSelected(), "Selected shape remained after Remove");
        }
        assertEquals(numTotal - numSelected, ex4.getShape_Collection().size(), "Shape collection size changed incorrectly");
    }

    @RepeatedTest(TestConsts.TESTS)
    void testCopy() {
        int numSelected = 0;
        int numTotal = ex4.getShape_Collection().size();
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            if (ex4.getShape_Collection().get(i).isSelected()) {
                numSelected += 1;
            }
        } 
        ex4.actionPerformed("Copy");
        ex4.mouseClicked(Point2D.ORIGIN);
        ex4.mouseClicked(Point2D.ORIGIN);
        assertEquals(numTotal + numSelected, ex4.getShape_Collection().size(), "Shape collection size changed incorrectly");
    }

    @RepeatedTest(TestConsts.TESTS)
    void testMove() {
        Point2D p1 = Point2DTest.randPoint(), p2 = Point2DTest.randPoint();
        Point2D moveVec = p1.vector(p2);
        ShapeCollectionable expected = ex4.getShape_Collection().copy();
        for (int i = 0; i < expected.size(); ++i) {
            if (expected.get(i).isSelected()) {
                expected.get(i).getShape().move(moveVec);
            }
        }
        ex4.actionPerformed("Move");
        ex4.mouseClicked(p1);
        ex4.mouseClicked(p2);
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            assertEquals(expected.get(i), ex4.getShape_Collection().get(i), (ex4.getShape_Collection().get(i).isSelected() ? "Selected shape didn't move correctly" : "Not selected shape moved"));
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testRotate() {
        Point2D p1 = Point2DTest.randPoint(), p2 = Point2DTest.randPoint();
        double angleDegrees = p1.vector(p2).angleDegrees();
        ShapeCollectionable expected = ex4.getShape_Collection().copy();
        for (int i = 0; i < expected.size(); ++i) {
            if (expected.get(i).isSelected()) {
                expected.get(i).getShape().rotate(p1, angleDegrees);
            }
        }
        ex4.actionPerformed("Rotate");
        ex4.mouseClicked(p1);
        ex4.mouseClicked(p2);
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            assertEquals(expected.get(i), ex4.getShape_Collection().get(i), (ex4.getShape_Collection().get(i).isSelected() ? "Selected shape didn't move correctly" : "Not selected shape moved"));
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testScale90() {
        Point2D p1 = Point2DTest.randPoint();
        ShapeCollectionable expected = ex4.getShape_Collection().copy();
        for (int i = 0; i < expected.size(); ++i) {
            if (expected.get(i).isSelected()) {
                expected.get(i).getShape().scale(p1, 0.9);
            }
        }
        ex4.actionPerformed("Scale_90%");
        ex4.mouseClicked(p1);
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            assertEquals(expected.get(i), ex4.getShape_Collection().get(i), (ex4.getShape_Collection().get(i).isSelected() ? "Selected shape didn't move correctly" : "Not selected shape moved"));
        }
    }
    @RepeatedTest(TestConsts.TESTS)
    void testScale110() {
        Point2D p1 = Point2DTest.randPoint();
        ShapeCollectionable expected = ex4.getShape_Collection().copy();
        for (int i = 0; i < expected.size(); ++i) {
            if (expected.get(i).isSelected()) {
                expected.get(i).getShape().scale(p1, 1.1);
            }
        }
        ex4.actionPerformed("Scale_110%");
        ex4.mouseClicked(p1);
        for (int i = 0; i < ex4.getShape_Collection().size(); ++i) {
            assertEquals(expected.get(i), ex4.getShape_Collection().get(i), (ex4.getShape_Collection().get(i).isSelected() ? "Selected shape didn't move correctly" : "Not selected shape moved"));
        }
    }
}
