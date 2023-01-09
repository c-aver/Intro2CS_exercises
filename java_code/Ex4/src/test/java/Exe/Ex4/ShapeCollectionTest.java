/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Rect2D;
import Exe.Ex4.geo.ShapeComp;

public class ShapeCollectionTest {
    ShapeCollection col;

    public static ShapeCollection randShapeCollection(boolean allowRotatedRect) {
        ShapeCollection ans = new ShapeCollection();
        int num = (int) (Math.random() * 17);
        for (int i = 0; i < num; ++i) {
            GUIShape sh = GUIShapeTest.randGuiShape(allowRotatedRect);
            sh.setTag(i + 1);
            ans.add(sh);
        }
        return ans;
    }

    @BeforeEach
    void beforeEach() {
        col = randShapeCollection(true);
    }

    @RepeatedTest(TestConsts.TESTS)
    void testCopy() {
        ShapeCollection copy = (ShapeCollection) col.copy();
        assertNotEquals(System.identityHashCode(col), System.identityHashCode(copy), "ShapeCollection copy is shallow");
        for (int i = 0; i < copy.size(); ++i) {
            assertEquals(col.get(i), copy.get(i), "ShapeCollection copy elements should be (logically) equal");
            assertNotEquals(System.identityHashCode(col.get(i)), System.identityHashCode(copy.get(i)), "ShapeCollection copy element is shallowly copied");
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testGetBoundingBox() {
        Rect2D boundingBox = col.getBoundingBox();
        for (int i = 0; i < col.size(); ++i) {
            GUI_Shapeable sh = col.get(i);
            for (Point2D p : sh.getShape().getPoints()) {
                assertTrue(boundingBox.contains(p), "Shape point is not contained in bounding box");
            }
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testSaveLoad() {
        ShapeCollection copy = (ShapeCollection) col;
        col.save("Saves/Test_Save");
        col.removeAll();
        assertEquals(0, col.size(), "Didn't properly clear");
        col.load("Saves/Test_Save");
        assertEquals(copy.size(), col.size(), "Didn't load correct number of shapes");
        for (int i = 0; i < col.size(); ++i) {
            assertEquals(copy.get(i), col.get(i), "Didn't load correct shape");
        }

    }

    // This is kinda copied directly from Ex4Test
    @RepeatedTest(TestConsts.TESTS)
    void testSort() {
        String[] allowedSorts = new String[] { "Area", "Perimeter", "ToString", "Tag" };
        String randSort = allowedSorts[(int) (Math.random() * allowedSorts.length)];
        boolean anti = Math.random() < 0.5;
        String action = (anti ? "ByAnti" : "By") + randSort;
        Comparator<GUI_Shapeable> randComp = null;
        switch (action) {
            case "ByArea": randComp = ShapeComp.CompByArea;                   break;
            case "ByPerimeter": randComp = ShapeComp.CompByPerimeter;         break;
            case "ByToString": randComp = ShapeComp.CompByToString;           break;
            case "ByTag": randComp = ShapeComp.CompByTag;                     break;
            case "ByAntiArea": randComp = ShapeComp.CompByAntiArea;           break;
            case "ByAntiPerimeter": randComp = ShapeComp.CompByAntiPerimeter; break;
            case "ByAntiToString": randComp = ShapeComp.CompByAntiToString;   break;
            case "ByAntiTag": randComp = ShapeComp.CompByAntiTag;             break;
        }
        col.sort(randComp);
        for (int i = 0; i < col.size() - 1; ++i) {
            assertTrue(randComp.compare(col.get(i), col.get(i + 1)) <= 0);
        }
    }

    @RepeatedTest(TestConsts.TESTS)
    void testString() {
        if (col.size() == 0) {
            assertEquals("", col.toString(), "Empty collection should return empty string");
            return;
        }
        ShapeCollectionable copy = col.copy();
        String str = col.toString();
        col.removeAll();
        assertEquals(0, col.size(), "Didn't properly clear");
        String[] lines = str.split("\n");
        for (String line : lines) {
            col.add(new GUIShape(line));
        }
        for (int i = 0; i < col.size(); ++i) {
            assertEquals(copy.get(i), col.get(i), "String conversion changed shape");
        }
    }
}
