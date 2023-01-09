/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4.gui;

import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionTest;
import Exe.Ex4.TestConsts;
import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Point2DTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.w3c.dom.events.MouseEvent;

/*
 * To test:
 * Clear, load, save
 * Colored drawing, filled/unfilled drawing
 * Move, copy, remove, rotate, scale 90%, scale 110%
 * Segment drawing, circle drawing, rect drawing, triangle drawing, polygon drawing
 * Sort by tag, antitag, area, antiarea, perimeter, antiperimter, tostring, antitostring
 * 
 */
public class Ex4Test {
    Ex4 ex4;

    // private MouseEvent moveMouseTo(Point2D p) {
    //     MouseEvent ans = new 
    // }

    private void randInit() {
        ex4 = Ex4.getInstance();
        ShapeCollection col = ShapeCollectionTest.randShapeCollection();
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
}
