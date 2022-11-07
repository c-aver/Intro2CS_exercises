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
	 * @param xx an array of x values of points
	 * @param yy an array of y values of points
	 * @return an array of doubles representing the coefficients of the polynomial which goes through the points
	 */
	public static double[] PolynomFromPoints(double[] xx, double[] yy) {
		double [] ans = null;
		assert false : "Not implemented";   // TODO: implement
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
	 * @param poly the polynomial to be evaluated
	 * @param x the value at which to evaluate
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
		String ans = "";							// initialize an empty string for the answer

		for (int i = poly.length - 1; i >= 0; --i) {				// iterate on the coefficients
			if (poly[i] >= 0.0 && i != poly.length - 1) ans += "+";	// if the coefficient is non-negative, add a plus sign (minus sign is automatically added for negatives), unless it is the first printed term
			ans += poly[i];											// add the coefficient to the result string
			if (i == 1) ans += "x ";									// if we are on the x term, add only the letter x to the result string
			else if (i > 1)											// if we are on a higher power
				ans += "x^" + i + " ";								// add x raised to the power of the current degree
		}
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
		assert false : "Not implemented";   // TODO: implement
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
		assert x1 < x2 : "Cannot find root in negative range"; // TODO: assert that there is solution
		double x = (x1+x2)/2;							// the first x we will check is the middle of the range,
														// from there we will close in on the true answer

		double[] p_ = derivative(p);					// calculate the derivative of our polynomial, useful for getting the direction of movement (up or down)
		while (Math.abs(f(p, x)) > eps) {				// iterate as long as our answer is not close enough to 0
			
			if (f(p, x) * f(p_, x) >= 0)				// if the function times the derivative is positive, we are either on a rising function with the 0 being lower or a falling function with the 0 being higher
				x2 = x;									// we move the range (x1-x2) to (x1-x), effectively cutting the right side half, since the 0 is on our left
			else										// if the function times the derivative is negative, we are either on a rising function with the 0 being higher or a falling function with the 0 being lower
				x1 = x;									// we move the range (x1-x2) to (x-x2), effectively cutting the left side half, since the 0 is on our right
			x = (x1+x2)/2;								// recompute the middle of the range
		}

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
		assert x1 < x2 : "Cannot find root in negative range"; // TODO: assert that there is solution
		double x = (x1+x2)/2;				// get the middle of the range (could be any other point in the range)
		if (Math.abs(f(p, x)) < eps)		// if its value under p is within the accepted range
			return x;						// return it as the answer

		double[] p_ = derivative(p);		// calculate the derivative of our polynomial, useful for getting the direction of movement (up or down)

		if (f(p, x) * f(p_, x) >= 0)		// if the function times the derivative is positive, we are either on a rising function with the 0 being lower or a falling function with the 0 being higher,
			return root_rec(p, x1, x, eps);	// this means the root is on the left side half (x1, x) of the range, so we look for it there
											// if we didn't return on the last line, the root is on the right side of the range
		return root_rec(p, x, x2, eps);		//	look for the root on the right side of the range (x, x2)
	}
	/**
	 * Given two polynoms (p1,p2), a range [x1,x2] and an integer representing the number of "boxes". 
	 * This function computes an approximation of the area between the polynoms within the x-range.
	 * The area is computed using Riemann's like integral (https://en.wikipedia.org/wiki/Riemann_integral)
	 * @param p1 - first polynom
	 * @param p2 - second polynom
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param numberOfBoxes - a natural number representing the number of boxes between x1 and x2.
	 * @return the approximated area between the two polynoms within the [x1,x2] range.
	 */
	public static double area(double[] p1,double[]p2, double x1, double x2, int numberOfBoxes) {
		assert x1 < x2 : "Cannot calculate area in negative range";
		double dx = (x2 - x1) / numberOfBoxes;							// the interval on which we need to calculate the box area

		double ans = 0;

		for (int i = 0; i < numberOfBoxes; ++i) {
			x2 = x1 + dx;												// set the end of the range (of our current box) to dx (the width of the box) after x1 (the start of the box)
			double x = (x1 + x2) / 2;									// set the value to be computed as the middle of the range
			double box_height = Math.abs(f(p1, x) - f(p2, x));			// calculate the height of the box between the two functions, which is just the difference of their height
			double box_area = box_height * dx;							// calculate the area of the box, its height times its width (which is dx)
			ans += box_area;											// add the area to the total answer
			x1 = x2;													// move the left of the range to the right, to be expanded on the next iteration
		}
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
		assert false : "Not implemented";   // TODO: implement
		return ZERO;
	}
	/**
	 * This function computes the polynom which is the sum of two polynoms (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] add(double[] p1, double[] p2) {
		double shorter[] = p1, longer[] = p2;		// assume p1 is the shorter of the polynomials
		if (p1.length > p2.length) {				// if assumption is incorrect
			shorter = p2;							// set the arrays accordingly
			longer = p1;							// "
		}

		double[] ans = new double[longer.length];	// initialize answer as long as the longer of the polynomials (note: guaranteed to bin initialized with 0.0 by language spec)

		for (int i = 0; i < shorter.length; ++i)	// iterate on coefficient up to the shorter array (the part where both arrays have values)
			ans[i] = shorter[i] + longer[i];		// set the answer's coefficient to the sum of the coefficients of the two polynomials


		for (int i = shorter.length; i < longer.length; ++i) {	// iterate on the coefficients of the longer polynomial which are not in the shorter
			ans[i] = longer[i];									// set the answer coefficient as the coefficient of the longer polynomial (the short one is done so it has 0)
		}
		return ans;				// return the computed answer
	}
	/**
	 * This function computes the polynom which is the multiplication of two polynoms (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] mul(double[] p1, double[] p2) {

		double[] ans = new double[p1.length + p2.length - 1];	// initialize the answer with the degree being the sum of the two degrees (note that the size of the array is 1 higher than the degree), this is because the rank of the answer (the power of the biggest x) is the ranks of the multiplicants added (since x^n*x^m=x^(m+n))

		for (int i = 0; i < p1.length; ++i)					// iterate on both polynomial, since we need to multiply each coefficient from the first by each coefficient from the second
			for (int j = 0; j < p2.length; ++j)				// "
				ans[i + j] += p1[i] * p2[j];				// a*x^n * b*x^m = a*b^(m+n)

		return ans;
	}
	/**
	 * This function computes the derivative polynom.
	 * @param po
	 * @return
	 */
	public static double[] derivative (double[] po) {
		assert po.length > 0: "Cannot find derivative of polynomial with no terms";
		double[] ans = new double[po.length - 1]; 	// initalize an answer polynomial 1 degree lower than input
		for (int i = 0; i < ans.length; ++i) {		// iterate on the coefficients up to the second to last one
			ans[i] = (i + 1) * po[i + 1];			// each one is the next coefficient multiplied by its place
		}

		return ans;
	}
	///////////////////// Private /////////////////////
}