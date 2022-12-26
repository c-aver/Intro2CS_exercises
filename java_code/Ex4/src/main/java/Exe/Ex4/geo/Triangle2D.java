package Exe.Ex4.geo;

/**
 * This class represents a 2D Triangle in the plane.
 * Ex4: you should implement this class!
 * @author I2CS
 *
 */
public class Triangle2D implements GeoShapeable{
	Point2D _p1, _p2, _p3;

	public Triangle2D(Point2D p1, Point2D p2, Point2D p3) {
		_p1 = new Point2D(p1);
		_p1 = new Point2D(p2);
		_p1 = new Point2D(p3);
	}

	@Override
	public boolean contains(Point2D ot) { // TODO: this implementation is initial and untested
		// concept: make sure the point is closer to each of the triangle's point then the line opposite it
		boolean closerToP1 = ot.distance(_p1) < ot.distance(new Segment2D(_p2, _p3));
		boolean closerToP2 = ot.distance(_p2) < ot.distance(new Segment2D(_p1, _p3));
		boolean closerToP3 = ot.distance(_p3) < ot.distance(new Segment2D(_p1, _p2));

		return closerToP1 && closerToP2 && closerToP3;
	}

	@Override
	public double area() { // TODO: this function is untested and unverified
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
