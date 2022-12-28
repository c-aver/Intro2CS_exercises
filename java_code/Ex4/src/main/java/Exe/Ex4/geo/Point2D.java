
package Exe.Ex4.geo;

import Exe.Ex4.Ex4_Const;

/**
 * This class represents a 2D point in the plane.
 * Do NOT change this class! It would be used as is for testing.
 * Ex4: you should edit and update this class!
 * @author boaz.benmoshe
 */


public class Point2D implements GeoShapeable {
    //public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1,2), EPS=EPS2;
    public static final Point2D ORIGIN = new Point2D(0,0);
    private double _x, _y;
    public Point2D(double x, double y) {
    	_x = x; _y = y;
    }
    public Point2D(Point2D p) {
       this(p.x(), p.y());
    }
    public Point2D(String s) {
        this(s.split(","));
    }
    public Point2D(String[] args) {
        try {
            _x = Double.parseDouble(args[0]);
            _y = Double.parseDouble(args[1]);
        }
        catch(IllegalArgumentException e) {
            System.err.println("ERROR: Got wrongly formatted string for Point2D constructor. Got: \"" + args[0] + ',' + args[1] + "\", should be of format: \"x,y\"");
            throw(e);
        }
    }
    public double x() { return _x; }
    public double y() { return _y; }
 
    public int ix() { return (int) _x; }
    public int iy() { return (int) _y; }
  
    public Point2D add(Point2D p) {
    	Point2D a = new Point2D(p.x() + x(), p.y() + y());
    	return a;
    }
    public String toString() {
        return _x + "," + _y;
    }

    public double distance() {
        return this.distance(ORIGIN);
    }
    public double distance(Point2D p2) {
        double dx = this.x() - p2.x();
        double dy = this.y() - p2.y();
        double t = (dx*dx + dy*dy);
        return Math.sqrt(t);
    }
    /**
     * This function finds the point's distance from a line, represeneted as a Segment2D.
     * Note that this treats the Segment2D as an infinite line.
     * @param line the line to find the distance from, represented as a Segment2D
     * @return the distance of the point from the line
     */
    public double distance(Segment2D line) {
        // the next section is basically pulled directly from my Ex3 test (isTriangleLine)
        Point2D[] ps = line.getPoints();
		double x1 = ps[0].x(), y1 = ps[0].y(), x2 = ps[1].x(), y2 = ps[1].y();  // preparing the numbers for the formula
		double dx1 = x1 - _x, dx21 = x2 - x1, dy1 = y1 - _y, dy21 = y2 - y1;      // some of the deltas we will need
		double dist = (Math.abs(dx21 * dy1 - dx1 * dy21)) / (Math.sqrt(dx21 * dx21 + dy21 * dy21)); // caluculate the distance between the p3 and the line p1-p2 using https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
        return dist;
    }
    @Override
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Point2D)) { return false; }  // these conditions will always stop an object from equaling to this
        Point2D p = (Point2D) o;                 // cast the object to a Point2D for logical evaluation
        return ((_x == p._x) && (_y == p._y));   // the points are equal if both their coordinated are equal
    }
    /**
     * This function checks "close enough" equality.
     * @param p2 the point to compare to
     * @param eps the distance from which they are not considered equal
     * @return whether the points are close enough to equal
     */
    public boolean closeToEquals(Point2D p2, double eps)
    {
        return (this.distance(p2) < eps);        // the points are close enough to equal if their distance is less than the allowed epsilon
    }
    /**
     * This function checks "close enough" equality.
     * This function uses the built in Ex4 epsilon as the distance from which points are not equal.
     * @param p2 the point to compare to
     * @return whether the points are close enough to eqaul
     */
    public boolean closeToEquals(Point2D p2)
    {
        return closeToEquals(p2, Ex4_Const.EPS);  // check the same thing with the constant epsilon 
    }
    /**
     * This method returns the vector between this point and the target point. The vector is represented as a Point2D.
     * Note that the tail is this point and the head is the target
     * @param target the point to draw the vector to
     * @return the vector from this point to the target
     */
    public Point2D vector(Point2D target) {
    	double dx = target.x() - this.x();  // the vector is computed by moving the target closer to the origin by this's coordinates
    	double dy = target.y() - this.y();  // effectively, we move the target to the equivalent position in relation to the origin
    	return new Point2D(dx, dy);
    }
	
	public void move(Point2D vec) {
		this._x += vec.x();
		this._y += vec.y();
	}
    /**
     * This method scales the point by the ratio, in relation to the origin
     * This is very useful for points which represent a vector
     * Note: this method changes the inner state of the object
     * @param ratio the ration by which to scale the point
     */
    public void scale(double ratio) {
        this._x *= ratio;
        this._y *= ratio;
    }
    @Override
	public void scale(Point2D cen, double ratio) {
		Point2D directionVec = cen.vector(this);   // the vector pointing from the center to here, to be scaled be the ratio
        directionVec.scale(ratio);                 // scale the vector by the ratio, it now represents the vector from the center to where the point needs to be
        Point2D newPoint = cen.add(directionVec);  // the new point's location is the center moved by the scaled direction vector
        this._x = newPoint._x;  // set the point's coordinates to the new location
        this._y = newPoint._y;
	}
    /**
     * This method rotates the point by the angle provided, in relation to the origin
     * This is very useful for points which represent a vector
     * Note: this method changes the inner state of the object
     * @param angleDegrees the angle by which to rotate the point, in degrees
     */
    public void rotate(double angleDegrees) {
        double mag = this.distance();
        double oldAngleRadians = Math.atan2(_y, _x);
        double oldAngleDegrees = Math.toDegrees(oldAngleRadians);
        double newAngleDegrees = oldAngleDegrees + angleDegrees;
        double newAngleRadians = Math.toRadians(newAngleDegrees);
        _x = mag * Math.cos(newAngleRadians);
        _y = mag * Math.sin(newAngleRadians);
    }
    @Override
	public void rotate(Point2D cen, double angleDegrees) {
		Point2D directionVec = cen.vector(this);   // the vector pointing from the center to here, to be rotated
        directionVec.rotate(angleDegrees);         // rotate the vector by the angle, it now represents the vector from the center to where the point needs to be
        Point2D newPoint = cen.add(directionVec);  // the new point's location is the center moved by the scaled direction vector
        this._x = newPoint._x;  // set the point's coordinates to the new location
        this._y = newPoint._y;
	}
    /**
     * This method return the angle in degrees of the vector pointing at the point, in relation to the x axis
     * @return the angle in degrees
     */
    public double angleDegrees() {
        double angleRadians = Math.atan2(_y, _x);
        return Math.toDegrees(angleRadians);
    }
    @Override
    public boolean contains(Point2D ot) {
        return equals(ot);    // a point is considered to contain another point if they are equal
    }
    @Override
    public double area() {
        return 0;
    }
    @Override
    public double perimeter() {
        return 0;
    }
    @Override
    public GeoShapeable copy() {
        return new Point2D(this);
    }
    @Override
    public Point2D[] getPoints() {
        return new Point2D[] { this };
    }
   
}
