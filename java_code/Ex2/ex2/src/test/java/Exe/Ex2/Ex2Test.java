package Exe.Ex2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This JUnit class represents a very simple unit testing for Ex2.
 * @author c-aver
 * Name: Chaim Averbach
 * ID: 207486473
 */
public class Ex2Test {
  static double[] po1 = {2, 0, 3, -1};     // premade polynomials for testing
  static double[] po2 = {0.1, 0, 1, 0.1, 3};  // -"-

  @Test
  public void testEquals() {
    boolean eq1 = Ex2.equals(po1, new double[] {2, 0, 3, -1, 0});
    boolean eq2 = Ex2.equals(po1, new double[] {2, 0, 3, -1});
    boolean eq3 = Ex2.equals(po1, new double[] {2 + Ex2.EPS/2, 0 - Ex2.EPS/2, 3, -1 - Ex2.EPS/2});
    boolean eq4 = Ex2.equals(Ex2.ZERO, Ex2.ZERO);
    boolean neq1 = Ex2.equals(po1, po2);
    boolean neq2 = Ex2.equals(po1, new double[] {2, 0, 3, -1, 0, 5});
    boolean neq3 = Ex2.equals(po1, Ex2.ZERO);
    boolean neq4 = Ex2.equals(Ex2.ZERO, new double[] {});
    assertEquals(true, eq1);
    assertEquals(true, eq2);
    assertEquals(true, eq3);
    assertEquals(true, eq4);
    assertEquals(false, neq1);
    assertEquals(false, neq2);
    assertEquals(false, neq3);
    assertEquals(false, neq4);
    try {
      Ex2.equals(Ex2.ZERO, null);
      assert false : "Successfully checked equality on null array";
    } catch (AssertionError e) {
      assertEquals("Cannot check equality on null array", e.getMessage());
    }
  }

  @Test
  public void testF() {
    double fx0 = Ex2.f(po1, 0);
    double fx1 = Ex2.f(po1, 1);
    double fx2 = Ex2.f(po1, 2);
    assertEquals(2, fx0, Ex2.EPS);
    assertEquals(4, fx1, Ex2.EPS);
    assertEquals(6, fx2, Ex2.EPS);
    try {
      Ex2.f(null, 0);
      assert false : "Successfully evaluated null array";
    } catch (AssertionError e) {
      assertEquals("Cannot evaluate point on null array", e.getMessage());
    }
  }

  @Test
  public void testRoots() {
    double x12 = Ex2.root(po1, 0, 10, Ex2.EPS);
    double x12_rec = Ex2.root_rec(po1, 0, 10, Ex2.EPS);
    assertEquals(3.1958, x12, Ex2.EPS);
    assertEquals(3.1958, x12_rec, Ex2.EPS);
    try {
      Ex2.root(po1, 10, 0, Ex2.EPS);
      Ex2.root_rec(po1, 10, 0, Ex2.EPS);
      assert false : "Succeeded in finding root in non-positive range";
    } catch (AssertionError e) {
      assertEquals("Cannot find root in non-positive range", e.getMessage());
    }
  }
  
  @Test
  public void testMul() {
    double[] p12 = Ex2.mul(po1, po2);
    // -3 x^7 + 8.9 x^6 - 0.7 x^5 + 9 x^4 + 0.1 x^3 + 2.3 x^2 + 0.2 calculated by WolframAlpha
    assertArrayEquals(new double[] {0.2, 0.0, 2.3, 0.1, 9.0, -0.7, 8.9, -3.0}, p12);
    try {
      Ex2.mul(po1, null);
      assert false : "Successfully multiplied null array";
    } catch (AssertionError e) {
      assertEquals("Cannot multiply null array", e.getMessage());
    }
  }

  @Test
  public void testAdd() {
    double[] p12 = Ex2.add(po1, po2);
    assertArrayEquals(new double[] {2.1, 0, 4, -0.9, 3}, p12, Ex2.EPS);
    try {
      Ex2.add(po1, null);
      assert false : "Successfully added null array";
    } catch (AssertionError e) {
      assertEquals("Cannot add null array", e.getMessage());
    }
  }

  @Test
  public void testDerivative() {
    double[] cons = {1};
    double[] p = {1,2,3}; // 3x^2+2x+1
    double[] dp1 = {2,6}; // 6x+2
    double[] dp2 = Ex2.derivative(p);
    assertArrayEquals(Ex2.ZERO, Ex2.derivative(cons));
    assertArrayEquals(dp1, dp2, Ex2.EPS);
    try {
      Ex2.derivative(new double[] {});
      assert false : "Found dertivative of polynomial with 0 terms";
    } catch (AssertionError e) {
      assertEquals("Cannot find derivative of polynomial with 0 terms", e.getMessage());
    }
    try {
      Ex2.derivative(null);
      assert false : "Successfully found derivative of null array";
    } catch (AssertionError e) {
      assertEquals("Cannot find derivative of null array", e.getMessage());
    }
  }

  @Test
  public void testAreaBetweenPolys() {
    double a1 = Ex2.area(po1, po2, 0.0, 10.0, 10000);
    assertEquals(62067.72029, a1, Ex2.EPS);        // expected value calculated by Symbolab
    try {
      Ex2.area(po1, po2, 2, 0.5, 1);
      assert false : "Succeeded with non-positive range";
    } catch (AssertionError e) {
      assertEquals("Cannot calculate area in non-positive range", e.getMessage());
    }
    try {
      Ex2.area(po1, po2, 0.5, 2, -1);
      Ex2.area(po1, po2, 0.5, 2, 0);
      assert false : "Succeeded with negative number of boxes";
    } catch (AssertionError e) {
      assertEquals("Cannot use non-positive number of boxes", e.getMessage());
    }
    try {
      Ex2.area(po1, null, 0.0, 0.5, 5);
      assert false : "Successfully found area under null array";
    } catch (AssertionError e) {
      assertEquals("Cannot find area with null array", e.getMessage());
    }
  }

  @Test
  public void testSameValue() {
    double x12 = Ex2.sameValue(po1, Ex2.ZERO, 0, 10, Ex2.EPS);
    assertEquals(3.1958, x12, Ex2.EPS);
    double eqp = Ex2.sameValue(po1, po2, 0.5, 2, Ex2.EPS);
    assertEquals(0.981738, eqp, Ex2.EPS);          // expected value calculated by WoflramAlpha
    try {
      Ex2.sameValue(po1, po2, 2, 0.5, Ex2.EPS);
      assert false : "Succeeded with non-positive range";
    } catch (AssertionError e) {
      assertEquals("Cannot find equal value in non-positive range", e.getMessage());
    }
    try {
      Ex2.sameValue(po1, po2, 2, 2.1, Ex2.EPS);
      assert false : "Succeeded with same higher polynomial in each endpoint";
    } catch (AssertionError e) {
      assertEquals("The higher polynomial must be different at each endpoint", e.getMessage());
    }
  }

  @Test
  public void testString() {
    String spo0 = Ex2.poly(Ex2.ZERO);
    String spo1 = Ex2.poly(po1);
    String spo2 = Ex2.poly(po2);
    boolean po0eq = Ex2.equals(Ex2.ZERO, Ex2.getPolynomFromString(spo0));
    boolean po1eq = Ex2.equals(po1, Ex2.getPolynomFromString(spo1));
    boolean po2eq = Ex2.equals(po2, Ex2.getPolynomFromString(spo2));
    assertEquals(true, po0eq);
    assertEquals(true, po1eq);
    assertEquals(true, po2eq);
    try {
      Ex2.poly(new double[] {});
      assert false : "Converted 0-term polynomial to string";
    } catch (AssertionError e) {
      assertEquals("Cannot convert 0-term polynomial to string", e.getMessage());
    }
    String[] illegalStrings = {"Hello, World!", "xxxx", "5.0x^3.5", "", "  "};
    for (String string : illegalStrings) {
      try {
        Ex2.getPolynomFromString(string);
        assert false : "Successfully parsed illegal string";
      } catch (AssertionError e) {
        assertEquals("Illegaly foramtted string was provided", e.getMessage());
      }
    }
    try {
      Ex2.poly(null);
    } catch (AssertionError e) {
      assertEquals("Cannot convert null array to string", e.getMessage());
    }
    
  }

  @Test
  public void testCofactor() {
    double[][] mat3 = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
    double[][] mat4 = {{1, 4, 7, 2}, {3, 0, 5, -3}, {-1, 9, 11, 4}, {4, 2, -7, 4}};
    double [][] cofacs3 = Ex2.cofactorMatrix(mat3);
    double [][] cofacs4 = Ex2.cofactorMatrix(mat4);
    assertArrayEquals(new double[][] {{-45.0, -38.0, 27.0}, {19.0, 18.0, -13.0}, {20.0, 16.0, -12.0}}, cofacs3);
    assertArrayEquals(new double[][] {{115.0, -427.0, 198.0, 445.0}, {78.0, 138.0, -32.0, -203.0}, {-66.0, 251.0, -78.0, -196.0}, {67.0, 66.0, -45.0, -8.0}}, cofacs4);
  }

  @Test
  public void testTranspose() {
    double[][] mat = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
    double [][] transposed = Ex2.transpose(mat);
    assertArrayEquals(new double[][] {{1.0, 3.0, -1.0}, {4.0, 0.0, 9.0}, {7.0, 5.0, 11.0}}, transposed);
  }

  @Test
  public void testDeterminant() {
    double[][] mat = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
    double det = Ex2.det(mat);
    assertEquals(-8.0, det, Ex2.EPS);
  }

  @Test
  public void testInvert() {
    double[][] mat3 = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
    double[][] mat4 = {{1, 4, 7, 2}, {3, 0, 5, -3}, {-1, 9, 11, 4}, {4, 2, -7, 4}};
    double[][] inv_mat3 = Ex2.invert(mat3);
    double[][] inv_mat4 = Ex2.invert(mat4);
    double[][] expected4 = new double[][] {{ 0.168374,  0.114202, -0.096632,  0.098096}, 
                                           {-0.625183,  0.202049,  0.367496,  0.096632},
                                           { 0.289897, -0.046852, -0.114202, -0.065885},
                                           { 0.651537, -0.297218, -0.286868, -0.011713}};
    assertArrayEquals(new double[][] {{5.625, -2.375, -2.5}, {4.75, -2.25, -2.0}, {-3.375, 1.625, 1.5}}, inv_mat3);
    for (int i = 0; i < inv_mat4.length; ++i) {
      assertArrayEquals(expected4[i], inv_mat4[i], Ex2.EPS);
    }
  }

  @Test
  public void testMatMul() {
    double[][] mat = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
    double[] vec = {5.5, -2.0, 7.0};
    double[] mul = Ex2.vec_mul(mat, vec);
    assertArrayEquals(new double[] {46.5, 51.5, 53.5}, mul, Ex2.EPS);
  }

  @Test
  public void testPolynomFromPoints() {
    double[] xx3 = {22.3, -52.2, 14.3};
    double[] yy3 = {41.2, 21.2, -32.0};
    double[] poly3 = Ex2.PolynomFromPoints(xx3, yy3);
    boolean eq3 = Ex2.equals(poly3, new double[] {-120.25499, 4.261812, 0.133557});
    assertEquals(true, eq3);
    double[] xx1 = {1.0};
    double[] yy1 = {1.0};
    double[] xx4 = {22.3, -52.2, 14.3, 12.7};
    double[] yy4 = {41.2, 21.2, -32.0, -8.0};
    double[] poly1 = Ex2.PolynomFromPoints(xx1, yy1);
    double[] poly4 = Ex2.PolynomFromPoints(xx4, yy4);
    if (Ex2.GENERALIZED) {
      boolean eq1 = Ex2.equals(poly1, new double[] {1.0});
      boolean eq4 = Ex2.equals(poly4, new double[] {490.716, -54.1568, 0.706134, 0.0367037});
      assertEquals(true, eq1);
      assertEquals(true, eq4);
    }
    else {
      assertEquals(null, poly1);
      assertEquals(null, poly4);
    }

    try {
      Ex2.PolynomFromPoints(xx3, yy4);
      assert false : "Succeeded with unequal number of x and y values";
    } catch (AssertionError e) {
      assertEquals("PolynomFromPoints got 3 x values and 4 y values, they must be equal", e.getMessage());
    }
  }
}