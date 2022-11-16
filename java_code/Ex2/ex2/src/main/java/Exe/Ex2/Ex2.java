package Exe.Ex2;

/** 
 * This class represents a set of functions on a polynomial - represented as array of doubles.
 * @author c-aver
 * Name: Chaim Averbach
 * ID: 207486473
 */
public class Ex2 {
  /** Epsilon value for numerical computation, it serves as a "close enough" threshold. */
  public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
  /** The zero polynomial is represented as an array with a single (0) entry. */
  public static final double[] ZERO = {0};

  /**
   * This function computes a polynomial representation from a set of 2D points on the polynom.
   * Note: this function works for any number of points.
   * @param xx an array of x values of points
   * @param yy an array of y values of points
   * @return an array of doubles representing the coefficients of the polynomial which goes through the points
   */
  public static double[] PolynomFromPoints(double[] xx, double[] yy) {
    double [] ans = null;
    if (xx == null || yy == null)
      return null;
    int n = xx.length;
    assert yy.length == n : "Unequal number of x and y values was given";
    // this function, in essence, solves a system of equations with a_i as the coefficients of the result polynomial:
    // a_2*x_1^2+a_1*x_1+a_0 = y_1
    // a_2*x_2^2+a_1*x_2+a_0 = y_2 
    // a_2*x_3^2+a_1*x_3+a_0 = y_3
    // so we will solve it with linear algebra
    double[][] coefficients = new double[n][n];   // initialize the matrix of coefficients, note that this is the equation system's coefficients, not the polynomials'
    for (int i = 0; i < n; ++i) {                 // iterate on the matrix of coefficients
      for (int j = 0; j < n; ++j)                 // "
        coefficients[i][j] = Math.pow(xx[i], j);  // each coefficient is the x value in the current equation raised to the power of its place in the equation
    }
    double[] constants = new double[n];           // initialize the vector of constant for the equation system
    for (int i = 0; i < n; ++i) {                 // iterate on the vector
      constants[i] = yy[i];                       // each constant is the solution to the equation, which is the y value of the point
    }
    double[][] coeff_inv = invert(coefficients);  // compute the inverse of the coefficient matrix to be multiplied by 
    ans = vec_mul(coeff_inv, constants);          // the answer is the multiplication of the inverted coefficient matrix by the constant matrix, if you want to know why take Linear Algebra 1
    return ans;                                   // return the computed answer
  }
  /**
   * This function multiplies a matrix by a vector
   * Note that the params must be double[n][n] mat and double[n] vec
   * @param mat the matrix to be multiplied
   * @param vec the vector to multiply in
   * @return the vector answer of the multiplication
   */
  public static double[] vec_mul(double[][] mat, double[] vec) {
    int n = vec.length;
    assert mat.length == n && mat[0].length == n : "Cannot multiply vector and matrix of different sizes";  // assert that user abides by function limitations, this is not a fool-proof assertion ({{1, 2}, {1}} fools it) but should be good enough
    double[] ans = new double[n];          // intialize the result as a vector of size n
    for (int i = 0; i < n; ++i)            // iterate on the cells of the answer vector
      for (int j = 0; j < n; j++)          // iterate on the columns of mat
        ans[i] += vec[j] * mat[i][j];      // sum up the vector multiplication of vec and mat[i]

    return ans;    // return the computed vector
  }
  /**
   * This function computes a the cofactor matrix for a nxn matrix.
   * This uses the method decscribed in https://en.wikipedia.org/wiki/Minor_(linear_algebra)#First_minors
   * @param mat the matrix for which to find the cofactors
   * @return the cofactor matrix for mat
   */
  public static double[][] cofactorMatrix(double[][] mat) {
    int n = mat.length;                                                 // get matrix size
    assert mat[0].length == n : "Cannot create cofactor matrix for non-square matrix";
    double[][] ans = new double[n][n];                                  // initialize the answer matrix
    for (int i = 0; i < n; ++i)                                         // we will iterate on each number in the matrix to set it in the ans
      for (int j = 0; j < n; ++j) {                                     // "
        int sign = ((i + j) % 2 == 0 ? 1 : -1 );                        // this is the sign that the minor needs to be multiplied in, depending on the parity of i + j
        double[][] submatrix = new double[n - 1][n - 1];                // this is the submatrix needed to compute the minor, one smaller in each dimension from the original
        for (int k = 0; k < n - 1; ++k)                                 // iterate on the submatrix cells
          for (int l = 0; l < n - 1; ++l)                               // "
            submatrix[k][l] = mat[(k >= i ? k + 1 : k)][(l >= j ? l + 1 : l)];  // set the submatrix cell by matrix cell, if we are to the right or below the excluded cell add 1 to the index from which we are taking the value
        double m_i_j = det(submatrix);                                  // this is the minor for cell i, j, computed as the determinant of the submatrix
        ans[i][j] = m_i_j * sign;                                       // the cofactor is the minor multiplied by the sign
      }

    return ans;                                                         // after computing all cofactors we can return the answer
  }
  /**
   * This functions transposes a nxn matrix
   * @param mat the matrix to transposed
   * @return the transposed matrix
   */
  public static double[][] transpose(double[][] mat) {
    int n = mat.length;                  // find size of matrix
    assert mat[0].length == n : "Cannot transpose non-square matrix";
    double [][] ans = new double[n][n];  // initialize the answer matrix
    for (int i = 0; i < n; ++i)          // iterate on cells of answer matrix
      for (int j = 0; j < n; ++j)        // "
        ans[i][j] = mat[j][i];           // set the cell to be the transposed cell from the original
    return ans;                          // return the tranposed matrix
  }
  /**
   *  This function find the inverse of a nxn matrix
  * We will do this using the method described in https://en.wikipedia.org/wiki/Invertible_matrix#Analytic_solution
   * @param mat the matrix to be inverted
   * @return  the inverse
   */

  public static double[][] invert(double[][] mat) {
    int n = mat.length;                       // find size of matrix
    assert mat[0].length == n : "Cannot invert non-square matrix";
    double[][] ans = new double[n][n];        // initialize result matrix
    double[][] cofacs = cofactorMatrix(mat);  // first compute the cofactor matrix
    double[][] cofacs_T = transpose(cofacs);  // then tranpose the cofactor matrix
    double det_ops = 1.0 / det(mat, cofacs);  // compute the inverse of the determinant of the matrix
    for (int i = 0; i < n; ++i)               // iterate on the cells of the answer
      for (int j = 0; j < n; j++)             // "
        ans[i][j] = det_ops * cofacs_T[i][j]; // set the answer as the transposed cofactor multiplied by the opposite of the determinant
    return ans;                               // return the computed result
  }
  /**
   * This functions computes the determinant of a nxn matrix, given the matrix of cofactors
   * This function uses the methed described in https://en.wikipedia.org/wiki/Determinant
   * @param mat the matrix for which to compute the determinant
   * @param cofacs the cofactor matrix, if null will be computed from mat
   * @return the determinant of the matrix
   */
  public static double det(double[][] mat, double[][] cofacs) {
    if (cofacs == null) return det(mat);                              // check if cofacs were not provided and redirect to the appropriate function, for backwards compatibility
    if (mat.length == 2 && mat[0].length == 2 && mat[1].length == 2)  // the base case of a 2x2 matrix in which the determinant is a simple computation
      return mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];           // the 2x2 determinant formula
    double ans = 0.0;                                                 // initialize answer as 0
    for (int i = 0; i < mat.length; ++i)                              // iterate on cells of the first row
      ans += mat[i][0] * cofacs[i][0];                                // add the cell times the cofactor to the answer
    return ans;                                                       // return the computed answer
  }
  /**
   * This functions computes the determinant of a nxn matrix
   * This overloads the previous method and computes the cofactors itself
   * This function uses the methed described in https://en.wikipedia.org/wiki/Determinant
   * @param mat the matrix for which to compute the determinant
   * @param cofacs the cofactor matrix, if null will be computed from mat
   * @return the determinant of the matrix
   */
  public static double det(double[][] mat) {
    if (mat.length == 2 && mat[0].length == 2 && mat[1].length == 2)  // the base case of a 2x2 matrix in which the determinant is a simple computation
      return (mat[0][0] * mat[1][1]) - (mat[0][1] * mat[1][0]);       // the 2x2 determinant formula
    double ans = 0.0;                                                 // initialize answer as 0
    double[][] cofacs = cofactorMatrix(mat);                          // this function does not take cofacs so we must compute them
    for (int i = 0; i < mat.length; ++i)                              // iterate on cells of the first row
      ans += mat[i][0] * cofacs[i][0];                                // add the cell times the cofactor to the answer
    return ans;                                                       // return the computed answer
  }

  /** Two polynoms are equal if and only if the have the same coefficients - up to an epsilon (aka EPS) value.
   * @param p1 first polynom
   * @param p2 second polynom
   * @return true iff p1 represents the same polynom as p2.
   */
  public static boolean equals(double[] p1, double[] p2) {
    double shorter[] = p1, longer[] = p2;                   // assume p1 is the shorter of the polynomials
    if (p1.length > p2.length) {                            // if assumption is incorrect
      shorter = p2;                                         // set the arrays accordingly
      longer = p1;                                          // "
    }

    for (int i = 0; i < shorter.length; ++i) {              // iterate for the length of the shorter polynomial to compare coefficients
      if (Math.abs(shorter[i] - longer[i]) > EPS)  {        // make sure coefficients aren't too different
        return false;                                       // coefficients are diferrent, so the polynomials are not equal
      }
    }                                                       // now we know that all coeffcients are the same up to the shorter polynomial, we need to make sure the longer one only has leading 0s
    
    for (int i = shorter.length; i < longer.length; ++i) {  // iterate on the coefficients of the longer polynomial which are not in the shorter
      if (longer[i] > EPS) {                                // make sure the coefficient is near enough to 0
        return false;                                       // longer polynomial has a non-0 cofficient which is not in shorter polynomal, so they are different
      }
    }

    return true;                                            // if we made it so far, the polynomials are equal
  }
  /**
   * Computes the f(x) value of the polynom at x.
   * @param poly the polynomial to be evaluated
   * @param x the value at which to evaluate
   * @return f(x) - the polynom value at x.
   */
  public static double f(double[] poly, double x) {
    double ans = 0;                        // initialize answer as 0
    
    for (int i = 0; i < poly.length; ++i)  // iterate on parts of the polynomial
      ans += poly[i] * (Math.pow(x, i));   // add to the answer the coefficients multiplied by the current power of x

    return ans;                            // return the computed answer
  }
  /** 
   * Computes a String representing the polynom.
   * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
   * @param poly the polynom represented as an array of doubles
   * @return String representing the polynom: 
   */
  public static String poly(double[] poly) {
    String ans = "";                                         // initialize an empty string for the answer
    if (poly.length == 1 && poly[0] == 0.0)                  // check special case of zero polynomial
      return "0";                                            // in this case we want a "0" string
    for (int i = poly.length - 1; i >= 0; --i) {             // iterate on the coefficients
      if (poly[i] == 0.0)                                    // if the coefficient is 0
        continue;                                            // we just skip the term
      if (poly[i] > 0.0 && i != poly.length - 1) ans += "+"; // if the coefficient is non-negative, add a plus sign (minus sign is automatically added for negatives), unless it is the first printed term
      ans += poly[i];                                        // add the coefficient to the result string
      if (i == 1) ans += "x ";                               // if we are on the x term, add only the letter x to the result string
      else if (i > 1)                                        // if we are on a higher power
        ans += "x^" + i;                                     // add x raised to the power of the current degree
      if (i > 0)                                             // if we are not on the last term (int the order of the string)
        ans += " ";                                          // we add a space to seperate from the next term
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
    assert (f(p1, x1) - f(p2, x1))*(f(p1, x2) - f(p2, x2)) <= 0 : "The higher polynomial must be different at each endpoint";    // NOTE: this assertion guarantees an answer, but can also fail when there is an answer, this will happen if the polynomials cross an even number of times within the range

    double x = (x1+x2)/2;                          // find the midpoint of the range as a starting point
    
    double higherAtx1 = (f(p1, x1) - f(p2, x1));   // this value is positive if p1 is higher at x1 and negative if p2 is higher at x1

    while (Math.abs(f(p1, x) - f(p2, x)) > EPS) {
      x = (x1+x2)/2;                               // recalculate midpoint
      double higherAtx = f(p1, x) - f(p2, x);      // check which polynomial is higher at x using the same method as the check at x1, this will be useful for knowing which side the equality is on
      if (higherAtx * higherAtx1 <= 0)             // if the multiplication is negative, differnet polynomial are higher at x1 and at x, which means the turning point is closer to x1
        x2 = x;                                    // and so we cut our range to (x1, x)
      else                                         // in the other case, the same polynomial is higher at x1 as at x1, so the turning point is closer to x2
        x1 = x;                                    // so we cut our range to (x, x2)
    }

    return x;
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
                                        // this function is implemented differently from sameValue (and from what the exercise expects, I presume), which gives it some advantages and disadvantages, it will work on any number of intersections with the x-axis but will get stuck on local minima above the x-axis or maxima below it
    double x = (x1+x2)/2;               // the first x we will check is the middle of the range,
                                        // from there we will close in on the true answer

    double[] p_ = derivative(p);        // calculate the derivative of our polynomial, useful for getting the direction of movement (up or down)
    while (Math.abs(f(p, x)) > eps) {   // iterate as long as our answer is not close enough to 0
      
      if (f(p, x) * f(p_, x) >= 0)      // if the function times the derivative is positive, we are either on a rising function with the 0 being lower or a falling function with the 0 being higher
        x2 = x;                         // we move the range (x1-x2) to (x1-x), effectively cutting the right side half, since the 0 is on our left
      else                              // if the function times the derivative is negative, we are either on a rising function with the 0 being higher or a falling function with the 0 being lower
        x1 = x;                         // we move the range (x1-x2) to (x-x2), effectively cutting the left side half, since the 0 is on our right
      x = (x1+x2)/2;                    // recompute the middle of the range
    }

    return x;                           // since we exited the loop f(p,x) is sufficiently close to 0
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
    assert x1 < x2 : "Cannot find root in negative range";
    // this function is implemented differently from sameValue (and from what the exercise expects, I presume), which gives it some advantages and disadvantages, it will work on any number of intersections with the x-axis but will get stuck on local minima above the x-axis or maxima below it
    double x = (x1+x2)/2;              // get the middle of the range (could be any other point in the range)
    if (Math.abs(f(p, x)) < eps)       // if its value under p is within the accepted range
      return x;                        // return it as the answer

    double[] p_ = derivative(p);       // calculate the derivative of our polynomial, useful for getting the direction of movement (up or down)

    if (f(p, x) * f(p_, x) >= 0)       // if the function times the derivative is positive, we are either on a rising function with the 0 being lower or a falling function with the 0 being higher,
      return root_rec(p, x1, x, eps);  // this means the root is on the left side half (x1, x) of the range, so we look for it there
                                       // if we didn't return on the last line, the root is on the right side of the range
    return root_rec(p, x, x2, eps);    //  look for the root on the right side of the range (x, x2)
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
    double dx = (x2 - x1) / numberOfBoxes;               // the interval on which we need to calculate the box area

    double ans = 0;                                      // initialize anwer as zero

    for (int i = 0; i < numberOfBoxes; ++i) {
      x2 = x1 + dx;                                      // set the end of the range (of our current box) to dx (the width of the box) after x1 (the start of the box)
      double x = (x1 + x2) / 2;                          // set the value to be computed as the middle of the range
      double box_height = Math.abs(f(p1, x) - f(p2, x)); // calculate the height of the box between the two functions, which is just the difference of their height
      double box_area = box_height * dx;                 // calculate the area of the box, its height times its width (which is dx)
      ans += box_area;                                   // add the area to the total answer
      x1 = x2;                                           // move the left of the range to the right, to be expanded on the next iteration
    }
    return ans;
  }
  /**
   * This function computes the array representation of a polynom from a String
   * representation. Note:given a polynom represented as a double array,  
   * getPolynomFromString(poly(p)) should return an array equals to p.
   * 
   * @param p - a String representing polynom.
   * @return  the parsed polynomial as an array of doubles
   */
  // string to parse: "0.0x^4 -1.0x^3 +3.0x^2 +0.0x +2.0"
  // after splitting: {"0.0x^4" ,"-1.0x^3", "+3.0x^2", "+0.0x", "+2.0" }
  public static double[] getPolynomFromString(String p) {
    String[] terms = p.split(" ");        // we split the string by " " to isolate the terms
    int polyDegree = getDegreeFromTerm(terms[0]);         // the degree of the polynomial is the degree of the first term (by definition the first term is the largest degree)
    
    double[] ans = new double[polyDegree + 1];            // initialize the answer array, note that array sizes are one larger than the degree of the polynomial

    for (String term : terms) {                           // iterate on the terms, we will parse each one seperately, I don't like foreach either but it's really convinient here
      int degree = getDegreeFromTerm(term);               // parse the degree from the term string
      double coefficient = getCoefficientFromTerm(term);  // parse the coefficient from the term
      ans[degree] = coefficient;                          // set the appropriate place in the answer to be the coefficient
    }
    
    return ans;
  }
  /**
   * This function takes a term of a polynomial as a string and returns its degree (the power to which x is raised)
   * 
   * @param term - a String representing a term of a polynomial
   * @return the degree of the term
   */
  private static int getDegreeFromTerm(String term) {
    String[] split_term = term.split("x");               // split the term into the garbage at the end ("x^n") and the actual coefficient
    if (split_term.length < 2)                           // this is the case where there was no garbage to begin with
      return 0;                                          // this means the power is 0, such as in "+2.0"
    if (split_term[1].length() == 0)                     // this is the case where there was nothing after the "x", such as in "+2.0x"
      return 1;                                          // so the degree is 1 (x=x^1)
    return Integer.parseInt(split_term[1].substring(1)); // in all other cases, the degree can be found by parsing the garbage except for the first character (which is '^'), so we return it
  }
  /**
   * This function takes a term of a polynomial as a string and returns its coefficient (the power to which x is raised)
   * 
   * @param term - a String representing a term of a polynomial
   * @return the coefficient of the term
   */
  private static double getCoefficientFromTerm(String term) {
    String[] split_term = term.split("x");    // split the term into the garbage at the end ("x^n") and the actual coefficient
    return Double.parseDouble(split_term[0]); // the coefficient can be found by parsing the garbage except for the first character (which is '^'), so we return it
  }

  /**
   * This function computes the polynomial which is the sum of two polynomials (p1,p2)
   * @param p1 first addend
   * @param p2 second addend
   * @return the polynomial sum of the two addends, as polynomials
   */
  public static double[] add(double[] p1, double[] p2) {
    double shorter[] = p1, longer[] = p2;  // assume p1 is the shorter of the polynomials
    if (p1.length > p2.length) {           // if assumption is incorrect
      shorter = p2;                        // set the arrays accordingly
      longer = p1;                         // "
    }

    double[] ans = new double[longer.length];  // initialize answer as long as the longer of the polynomials (note: guaranteed to be initialized with 0.0 by language spec)

    for (int i = 0; i < shorter.length; ++i)   // iterate on coefficient up to the shorter array (the part where both arrays have values)
      ans[i] = shorter[i] + longer[i];         // set the answer's coefficient to the sum of the coefficients of the two polynomials


    for (int i = shorter.length; i < longer.length; ++i) {  // iterate on the coefficients of the longer polynomial which are not in the shorter
      ans[i] = longer[i];                                   // set the answer coefficient as the coefficient of the longer polynomial (the short one is done so it has 0)
    }
    return ans;        // return the computed answer
  }
  /**
   * This function computes the polynom which is the multiplication of two polynoms (p1,p2)
   * @param p1 first multiplier
   * @param p2 second multiplier
   * @return the polynomial multiplication of the two parameters
   */
  public static double[] mul(double[] p1, double[] p2) {

    double[] ans = new double[p1.length + p2.length - 1];  // initialize the answer with the degree being the sum of the two degrees (note that the size of the array is 1 higher than the degree), this is because the rank of the answer (the power of the biggest x) is the ranks of the multiplicants added (since x^n*x^m=x^(m+n))

    for (int i = 0; i < p1.length; ++i)          // iterate on both polynomial, since we need to multiply each coefficient from the first by each coefficient from the second
      for (int j = 0; j < p2.length; ++j)        // "
        ans[i + j] += p1[i] * p2[j];             // a*x^n * b*x^m = a*b^(m+n)

    return ans;
  }
  /**
   * This function computes the derivative polynom.
   * @param po some polynomial
   * @return the polynomial representaion of the derivative of po
   */
  public static double[] derivative (double[] po) {
    assert po.length > 0: "Cannot find derivative of polynomial with no terms";
    double[] ans = new double[po.length - 1];   // initalize an answer polynomial 1 degree lower than input
    for (int i = 1; i < ans.length + 1; ++i) {  // iterate on the coefficients starting from the second one (first one is the constant which is cancelled by the derivative)
      ans[i - 1] = i * po[i];                   // the previous coefficient is the current one multiplied by its place
    }

    return ans;  // return the computed polynomial
  }
  ///////////////////// Private /////////////////////
}