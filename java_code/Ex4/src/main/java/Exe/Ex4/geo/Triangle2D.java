package Exe.Ex4.geo;

/**
 * This class represents a 2D Triangle in the plane.
 * Ex4: you should implement this class!
 * @author I2CS
 *
 */
public class Triangle2D implements GeoShapeable {
	Point2D _p1, _p2, _p3;

	public Triangle2D(Point2D p1, Point2D p2, Point2D p3) {
		_p1 = new Point2D(p1);
		_p2 = new Point2D(p2);
		_p3 = new Point2D(p3);
	}

	public Triangle2D(String[] args) {
		if (args.length < 6) throw new IllegalArgumentException("Can't initialize Triangle2D with less than 6 arguments");
		try {
			_p1 = new Point2D(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			_p2 = new Point2D(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
			_p3 = new Point2D(Double.parseDouble(args[4]), Double.parseDouble(args[5]));
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: Could not initialize Triangle2D: " + e.getMessage());
		}
    }

    @Override
	public String toString() {
		return "Triangle2D," + _p1 + ',' + _p2 + ',' + _p3;
	}

	@Override
	public boolean contains(Point2D ot) {
		// concept: make sure the point is close to each vertex's parallel line than the vertex is to the opposite line
		double dy3 = (_p1.y() - _p2.y()) / (_p1.x() - _p2.x());   // we calculate the line slopes to create a parallel line
		double dy2 = (_p1.y() - _p3.y()) / (_p1.x() - _p3.x());   // the slope is the default for dy
		double dy1 = (_p2.y() - _p3.y()) / (_p2.x() - _p3.x());
		int dx3 = 1;    // these are the x delta that will be added to create the lines
		int dx2 = 1;    // they are 1 by default to match with dy being the slope
		int dx1 = 1;
		if (Double.isInfinite(dy3)) { dy3 = 1; dx3 = 0; }  // for each of the dys, if they are infinite we need to change them to 1
		if (Double.isInfinite(dy2)) { dy2 = 1; dx2 = 0; }  // we also set dx to 0 to make the point directly above
		if (Double.isInfinite(dy1)) { dy1 = 1; dx1 = 0; }  // this will give a line with infinite slope

		Segment2D line3 = new Segment2D(_p3, new Point2D(_p3.x() + dx3, _p3.y() + dy3));   // create a new line, with the opposite point and a point on the same sloped line
		Segment2D line2 = new Segment2D(_p2, new Point2D(_p2.x() + dx2, _p2.y() + dy2));   // we create the point on the sloped line by adding 1 to x and k (AKA dy/dx) to y
		Segment2D line1 = new Segment2D(_p1, new Point2D(_p1.x() + dx1, _p1.y() + dy1));

		boolean closerToP1 = ot.distance(line1) < _p1.distance(new Segment2D(_p2, _p3));  // check that the point is closer to the parallel line that the point to its opposite line
		boolean closerToP2 = ot.distance(line2) < _p2.distance(new Segment2D(_p1, _p3));  // this means the point is not beyond the line
		boolean closerToP3 = ot.distance(line3) < _p3.distance(new Segment2D(_p1, _p2));

		return closerToP1 && closerToP2 && closerToP3;   // the point is contained if all 3 conditions are true
	}

	@Override
	public double area() {
		double base = _p1.distance(_p2);    // calculate the length of the triangle's base
		double height = _p3.distance(new Segment2D(_p1, _p2));   // the triangle's height is the distance of p3 from the line p1 -- p2
		return (base * height) / 2; // the formula for a traingle's area
	}

	@Override
	public double perimeter() {
		return _p1.distance(_p2) + _p2.distance(_p3) + _p3.distance(_p1);
	}

	@Override
	public void move(Point2D vec) {
		_p1.move(vec);
		_p2.move(vec);
		_p3.move(vec);
	}

	@Override
	public GeoShapeable copy() {
		return new Triangle2D(_p1, _p2, _p3);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		_p1.scale(center, ratio);
		_p2.scale(center, ratio);
		_p3.scale(center, ratio);
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		_p1.rotate(center, angleDegrees);
		_p2.rotate(center, angleDegrees);
		_p3.rotate(center, angleDegrees);
	}

	@Override
	public Point2D[] getPoints() {
		return new Point2D[] { _p1, _p2, _p3 };
	}
}
