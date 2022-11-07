package Exe.Ex2;
/** 
 * This class represents a set of functions on a polynom - represented as array of doubles.
 * @author boaz.benmoshe
 *
 */
public class Ex2 {
	/** Epsilon value for numerical computation, it servs as a "close enough" threshold. */
	public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
	/** The zero polynom is represented as an array with a single (0) entry. */
	public static final double[] ZERO = {0};
	/**
	 * This function computes a polynomial representation from a set of 2D points on the polynom.
	 * Note: this fuction only works for a set of points containing three points, else returns null.
	 * @param xx
	 * @param yy
	 * @return an array of doubles representing the coefficients of the polynom.
	 */
	public static double[] PolynomFromPoints(double[] xx, double[] yy) {
		double [] ans = null;
		assert false : "Not implemented";
		if(xx!=null && yy!=null && xx.length==3 && yy.length==3) {
		
		}
		return ans;
	}
	/** Two polynoms are equal if and only if the have the same coefficients - up to an epsilon (aka EPS) value.
	 * @param p1 first polynom
	 * @param p2 second polynom
	 * @return true iff p1 represents the same polynom as p2.
	 */
	public static boolean equals(double[] p1, double[] p2) {
		double shorter[] = p1, longer[] = p2;		// assume p1 is the shorter of the polynomials
		if (p1.length > p2.length) {			// if assumption is incorrect
			shorter = p2;				// set the arrays accordingly
			longer = p1;				// "
		}

		for (int i = 0; i < shorter.length; ++i) {	// iterate for the length of the shorter polynomial to compare coefficients
			if (Math.abs(shorter[i] - longer[i]) > EPS)	{		// make sure coefficients aren't different
				return false;						// coefficients are diferrent, so the polynomials are not equal
			}
		}											// now we know that all coeffcients are the same up to the shorter polynomial, we need to make sure the longer one only has leading 0s
		
		for (int i = shorter.length; i < longer.length; ++i) {	// iterate on the coefficients of the longer polynomial which are not in the shorter
			if (longer[i] > EPS) {								// make sure the coefficient is 0
				return false;									// longer polynomial has a non-0 cofficient which is not in shorter polynomal, so they are different
			}
		}

		return true;											// if we made it so far, the polynomials are equal
	}
	/**
	 * Computes the f(x) value of the polynom at x.
	 * @param poly
	 * @param x
	 * @return f(x) - the polynom value at x.
	 */
	public static double f(double[] poly, double x) {
		double ans = 0;							// initialize answer as 0
		
		for (int i = 0; i < poly.length; ++i)	// iterate on parts of the polynomial
			ans += poly[i] * (Math.pow(x, i));	// add to the answer the coefficients multiplied by the current power of x

		return ans;								// return the computed answer
	}
	/** 
	 * Computes a String representing the polynom.
	 * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
	 * @param poly the polynom represented as an array of doubles
	 * @return String representing the polynom: 
	 */
	public static String poly(double[] poly) {
		String ans = "";
		assert false : "Not implemented";
		return ans;
	}
	/**
	 * Given two polynoms (p1,p2), a range [x1,x2] and an epsilon eps. This function computes an x value (x1<=x<=x2)
	 * for which |p1(x) -p2(x)| < eps, assuming (p1(x1)-p2(x1)) * (p1(x2)-p2(x2)) <= 0.
	 * @param p1 - first polynom
	 * @param p2 - second polynom
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p1(x) -p2(x)| < eps.
	 */
	public static double sameValue(double[] p1, double[] p2, double x1, double x2, double eps) {
		double x12 = (x1+x2)/2;
		assert false : "Not implemented";
		return x12;
	}
	/**
	 * Given a polynom (p), a range [x1,x2] and an epsilon eps. 
	 * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps, 
	 * assuming p(x1)*p(x1) <= 0. 
	 * This function should be implemented iteratively (none recursive).
	 * @param p - the polynom
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
	 */
	public static double root(double[] p, double x1, double x2, double eps) {
		assert x1 < x2 : "Cannot find root in negative range";
		double x = (x1+x2)/2;							// the first x we will check is the middle of the range,
														// from there we will close in on the true answer

		while (Math.abs(f(p, x)) > eps) {				// iterate as long as our answer is not close enough to 0
			// double f_x1 = f(p, x1);	// TODO: needed?
			// double f_x2 = f(p, x2);
			
			if (f(p, x) * f(derivative(p), x) > 0)		// if the function times the derivative is positive, we are either on a rising function with the 0 being lower or a falling function with the 0 being higher
				x2 = x;									// we move the range (x1-x2) to (x1-x), effectively cutting the right side half, since the 0 is on our left
			else										// if the function times the derivative is negative, we are either on a rising function with the 0 being higher or a falling function with the 0 being lower
				x1 = x;									// we move the range (x1-x2) to (x-x2), effectively cutting the left side half, since the 0 is on our right
			x = (x1+x2)/2;								// recompute the middle of the range
		}

		assert false : "Not implemented";
		return x;										// since we exited the loop f(p,x) is sufficiently close to 0
	}
	/** Given a polynom (p), a range [x1,x2] and an epsilon eps. 
	 * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps, 
	 * assuming p(x1)*p(x1) <= 0. 
	 * This function should be implemented recursivly.
	 * @param p - the polynom
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
	 */
	public static double root_rec(double[] p, double x1, double x2, double eps) {
		double x12 = (x1+x2)/2;
		assert false : "Not implemented";
		return x12;
	}
	/**
	 * Given two polynoms (p1,p2), a range [x1,x2] and an integer representing the number of "boxes". 
	 * This function computes an approximation of the area between the polynoms within the x-range.
	 * The area is computed using Riemann's like integral (https://en.wikipedia.org/wiki/Riemann_integral)
	 * @param p1 - first polynom
	 * @param p2 - second polynom
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param numberOfBoxes - a natural number representing the number of boxes between xq and x2.
	 * @return the approximated area between the two polynoms within the [x1,x2] range.
	 */
	public static double area(double[] p1,double[]p2, double x1, double x2, int numberOfBoxes) {
		double ans = 0;
		assert false : "Not implemented";
		return ans;
	}
	/**
	 * This function computes the array representation of a polynom from a String
	 * representation. Note:given a polynom represented as a double array,  
	 * getPolynomFromString(poly(p)) should return an array equals to p.
	 * 
	 * @param p - a String representing polynom.
	 * @return
	 */
	public static double[] getPolynomFromString(String p) {
		assert false : "Not implemented";
		return ZERO;
	}
	/**
	 * This function computes the polynom which is the sum of two polynoms (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] add(double[] p1, double[] p2) {
		assert false : "Not implemented";
		return p1;
	}
	/**
	 * This function computes the polynom which is the multiplication of two polynoms (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] mul(double[] p1, double[] p2) {
		assert false : "Not implemented";
		return p1;
	}
	/**
	 * This function computes the derivative polynom.
	 * @param po
	 * @return
	 */
	public static double[] derivative (double[] po) {
		assert false : "Not implemented";
		return po;
	}
	///////////////////// Private /////////////////////
}