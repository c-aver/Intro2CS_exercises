/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Exe.Ex4.GUIShapeTest;
import Exe.Ex4.GUI_Shapeable;


public class ShapeCompTest {
    GUI_Shapeable g1, g2;
    
    @BeforeEach
    void beforeEach() {
        g1 = GUIShapeTest.randGuiShape(true);
        g2 = GUIShapeTest.randGuiShape(true);
        if (Math.random() < 1/3)   // add a certain chance for the object to be (shallowly) equal
            g2 = g1.copy();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByTag() {
        int comp = ShapeComp.CompByTag.compare(g1, g2);
        if (comp <  0) assertTrue(g1.getTag() <  g2.getTag());
        if (comp == 0) assertTrue(g1.getTag() == g2.getTag());
        if (comp >  0) assertTrue(g1.getTag() >  g2.getTag());
    }
    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByAntiTag() {
        int comp = ShapeComp.CompByAntiTag.compare(g1, g2);
        if (comp >  0) assertTrue(g1.getTag() <  g2.getTag());
        if (comp == 0) assertTrue(g1.getTag() == g2.getTag());
        if (comp <  0) assertTrue(g1.getTag() >  g2.getTag());
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByArea() {
        int comp = ShapeComp.CompByArea.compare(g1, g2);
        if (comp <  0) assertTrue(g1.getShape().area() <  g2.getShape().area());
        if (comp == 0) assertTrue(g1.getShape().area() == g2.getShape().area());
        if (comp >  0) assertTrue(g1.getShape().area() >  g2.getShape().area());
    }
    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByAntiArea() {
        int comp = ShapeComp.CompByAntiArea.compare(g1, g2);
        if (comp >  0) assertTrue(g1.getShape().area() <  g2.getShape().area());
        if (comp == 0) assertTrue(g1.getShape().area() == g2.getShape().area());
        if (comp <  0) assertTrue(g1.getShape().area() >  g2.getShape().area());
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByPerimeter() {
        int comp = ShapeComp.CompByPerimter.compare(g1, g2);
        if (comp <  0) assertTrue(g1.getShape().perimeter() <  g2.getShape().perimeter());
        if (comp == 0) assertTrue(g1.getShape().perimeter() == g2.getShape().perimeter());
        if (comp >  0) assertTrue(g1.getShape().perimeter() >  g2.getShape().perimeter());
    }
    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByAntiPerimeter() {
        int comp = ShapeComp.CompByAntiPerimter.compare(g1, g2);
        if (comp >  0) assertTrue(g1.getShape().perimeter() <  g2.getShape().perimeter());
        if (comp == 0) assertTrue(g1.getShape().perimeter() == g2.getShape().perimeter());
        if (comp <  0) assertTrue(g1.getShape().perimeter() >  g2.getShape().perimeter());
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByToString() {
        int comp = ShapeComp.CompByToString.compare(g1, g2);
        if (comp <  0) assertTrue(g1.toString().compareTo(g2.toString()) < 0);
        if (comp == 0) assertTrue(g1.toString().equals(g2.toString()));
        if (comp >  0) assertTrue(g1.toString().compareTo(g2.toString()) > 0);
    }
    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByAntiToString() {
        int comp = ShapeComp.CompByAntiToString.compare(g1, g2);
        if (comp <  0) assertTrue(g1.toString().compareTo(g2.toString()) > 0);
        if (comp == 0) assertTrue(g1.toString().equals(g2.toString()));
        if (comp >  0) assertTrue(g1.toString().compareTo(g2.toString()) < 0);
    }
}
