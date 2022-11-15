package Exe.Ex2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
/**
 * This JUnit class represents a very simple (and partial) unit testing for Ex2 - It should be improved and generalized significantly!
 * @author c-aver
 * Name: Chaim Averbach
 * ID: 207486473
 */

public class Ex2Test {
  static double[] po1={2, 0, 3, -1, 0};
  static double[] po2 = {0.1, 0, 1, 0.1, 3};

  @Test
  public void testEquals() {
    boolean eq1 = Ex2.equals(po1, new double[] {2, 0, 3, -1, 0});
    boolean eq2 = Ex2.equals(po1, new double[] {2, 0, 3, -1});
    boolean eq3 = Ex2.equals(po1, new double[] {2 + Ex2.EPS/2, 0 - Ex2.EPS/2, 3, -1 - Ex2.EPS/2});
    boolean neq1 = Ex2.equals(po1, po2);
    boolean neq2 = Ex2.equals(po1, new double[] {2, 0, 3, -1, 0, 5});
    assertEquals(true, eq1);
    assertEquals(true, eq2);
    assertEquals(true, eq3);
    assertEquals(false, neq1);
    assertEquals(false, neq2);
  }

  @Test
  public void testF() {
    double fx0 = Ex2.f(po1, 0);
    double fx1 = Ex2.f(po1, 1);
    double fx2 = Ex2.f(po1, 2);
    assertEquals(2, fx0, Ex2.EPS);
    assertEquals(4, fx1, Ex2.EPS);
    assertEquals(6, fx2, Ex2.EPS);
  }

  @Test
  public void testRoots() {
    double x12 = Ex2.root(po1, 0, 10, Ex2.EPS);
    double x12_rec = Ex2.root_rec(po1, 0, 10, Ex2.EPS);
    assertEquals(3.1958, x12, Ex2.EPS);
    assertEquals(3.1958, x12_rec, Ex2.EPS);
  }
  
  @Test
  public void testAdd() {
    double[] p12 = Ex2.add(po1, po2);
    double[] minus1 = {-1};
    double[] pp2 = Ex2.mul(po2, minus1);
    double[] p1 = Ex2.add(p12, pp2);
    assertEquals(Ex2.poly(po1), Ex2.poly(p1));
  }

  @Test
  public void testMulDoubleArrayDoubleArray() {
    double[] p12 = Ex2.add(po1, po2);
    double dd = Ex2.f(p12, 5);
    assertEquals(1864.6, dd, Ex2.EPS);
  }
  @Test
  public void testDerivativeArrayDoubleArray() {
    double[] p = {1,2,3}; // 3X^2+2x+1
    double[] dp1 = {2,6}; // 6x+2
    double[] dp2 = Ex2.derivative(p);
    assertEquals(dp1[0], dp2[0], Ex2.EPS);
    assertEquals(dp1[1], dp2[1], Ex2.EPS);
    assertEquals(dp1.length, dp2.length);
  }
  @Test
  public void testAreaBetweenPolys() {
    double a1 = Ex2.area(po1, po2, 0.0, 10.0, 100000);
    assertEquals(62067.72029, a1, Ex2.EPS);        // expected value calculated by Symbolab
  }
  @Test
  public void testSameValue() {
    double eqp = Ex2.sameValue(po1, po2, 0.5, 2, Ex2.EPS);
    assertEquals(0.981738, eqp, Ex2.EPS);          // expected value calculated by WoflramAlpha
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
    double[][] expected = new double[][] {{0.1683748, 0.114202049, -0.0966325, 0.0980966}, {-0.625183, 0.20204978, 0.367496, 0.0966325}, {0.28989751, -0.04685212, -0.11420204978, -0.06588579795}, {0.65153733, -0.297218155, -0.286868253294, -0.011713030746}};
    assertArrayEquals(new double[][] {{5.625, -2.375, -2.5}, {4.75, -2.25, -2.0}, {-3.375, 1.625, 1.5}}, inv_mat3);
    for (int i = 0; i < inv_mat4.length; ++i) {
      assertArrayEquals(expected[i], inv_mat4[i], Ex2.EPS);
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
    double[] xx4 = {22.3, -52.2, 14.3, 12.7};
    double[] yy4 = {41.2, 21.2, -32.0, -8.0};
    double[] poly3 = Ex2.PolynomFromPoints(xx3, yy3);
    double[] poly4 = Ex2.PolynomFromPoints(xx4, yy4);
    boolean eq3 = Ex2.equals(poly3, new double[] {-120.25499, 4.261812, 0.133557});
    boolean eq4 = Ex2.equals(poly4, new double[] {490.716, -54.1568, 0.706134, 0.0367037});
    assertEquals(true, eq3);
    assertEquals(true, eq4);
  }
}