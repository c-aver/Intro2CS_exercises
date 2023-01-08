/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;


public class ShapeCompTest {
    GeoShapeable g1, g2;
    
    @BeforeEach
    void beforeEach() {
        g1 = GeoShapeableTest.randShape();
        g2 = GeoShapeableTest.randShape();
    }

    @RepeatedTest(GeoTestConsts.TESTS)
    void testCompareByTag() {
        // TODO: implement
        assert false : "Not implemented";
    }
}
