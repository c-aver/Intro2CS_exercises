package Exe.Ex2;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 * This JUnit class represents a very simple (and partial) unit testing for Ex2 - It should be improved and generalized significantly!
 * @author boazben-moshe
 *
 */

public class Ex2Test {
	static double[] po1={2, 0, 3, -1, 0}, po2 = {0.1, 0, 1, 0.1, 3};

	@Test
	public void testEquals() {
		boolean eq1 = Ex2.equals(po1, new double[] {2, 0, 3, -1, 0});
		boolean eq2 = Ex2.equals(po1, new double[] {2, 0, 3, -1});
		boolean eq3 = Ex2.equals(po1, new double[] {2.0005, -0.0003, 3, -0.9999});
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
		double a1 = Ex2.area(po1, po2, 0.0, 10.0, 1000000);
		assertEquals(62067.72029, a1, Ex2.EPS);		// expected value calculated by Symbolab
	}
	@Test
	public void testSameValue() {
		double eqp = Ex2.sameValue(po1, po2, 0.5, 2, Ex2.EPS);
		assertEquals(0.981738, eqp, Ex2.EPS);					// expected value calculated by WoflramAlpha
	}
	@Test
	public void testString() {
		String spo1 = Ex2.poly(po1);
		String spo2 = Ex2.poly(po2);
		boolean po1eq = Ex2.equals(po1, Ex2.getPolynomFromString(spo1));
		boolean po2eq = Ex2.equals(po2, Ex2.getPolynomFromString(spo2));
		assertEquals(true, po1eq);
		assertEquals(true, po2eq);
	}
	@Test
	public void testCofactor() {
		double[][] mat = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
		double [][] cofacs = Ex2.cofactorMatrix(mat);
		assertArrayEquals(new double[][] {{-45.0, -38.0, 27.0}, {19.0, 18.0, -13.0}, {20.0, 16.0, -12.0}}, cofacs);
	}
	@Test
	public void testTranspose() {
		double[][] mat = {{1, 4, 7}, {3, 0, 5}, {-1, 9, 11}};
		double [][] transposed = Ex2.transpose(mat);
		assertArrayEquals(new double[][] {{1.0, 3.0, -1.0}, {4.0, 0.0, 9.0}, {7.0, 5.0, 11.0}}, transposed);
	}
}