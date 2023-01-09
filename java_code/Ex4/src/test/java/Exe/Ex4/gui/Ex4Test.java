/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4.gui;

import Exe.Ex4.Ex4_Const;
import Exe.Ex4.GUIShapeTest;
import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionTest;
import Exe.Ex4.TestConsts;
import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Point2DTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Button;

/* TODO:
 * To test:
 * Load, save
 * New shape colored drawing, filled/unfilled drawing
 * Move, copy, remove, rotate, scale 90%, scale 110%
 * Segment drawing, circle drawing, rect drawing, triangle drawing, polygon drawing
 * Sort by tag, antitag, area, antiarea, perimeter, antiperimter, tostring, antitostring
 * 
 */
/**
 * Note: this class does not test the specific methods in Ex4, instead it tests functionalities.
 * This is because the method are very interconnected, and it is somewhat weird to test only mouseMoved, for example.
 */
public class Ex4Test {
    Ex4 ex4;

    // Stolen for StdDraw_Ex4 to create MouseEvent
    // "Hard-coded" the constants and parameters
	private static double  scaleX(double x) { return 640 * (x - 0) / (Ex4_Const.DIM_SIZE - 0); }
	private static double  scaleY(double y) { return 640 * (y - 0) / (Ex4_Const.DIM_SIZE - 0); }

    private void moveMouseTo(Point2D p) {
        MouseEvent ev = new MouseEvent(new Button(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, (int) scaleX(p.x()), (int) scaleY(p.y()), 0, false);  // TODO: wrong x and y values
        ex4.mouseMoved(ev);
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
        sh.setColor(setRandColor());   // since randGuiShape gives illegal colors as well
        setFill(sh.isFilled());
        setShape(sh.getShape().getClass());
        for (Point2D p : sh.getShape().getPoints()) {
            moveMouseTo(p);
            ex4.mouseClicked(p);
        }
        assertEquals(sh, ex4.getShape_Collection().get(ex4.getShape_Collection().size() - 1), "Drawn shape is not the last in the colllection");
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
}
