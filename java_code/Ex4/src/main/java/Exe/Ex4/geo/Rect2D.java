/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Exe.Ex4.Ex4_Const;

/**
 * This class represents a 2D rectangle (NOT necessarily axis parallel - this shape can be rotated!)
 * Ex4: you should implement this class!
 * @author c-aver
 *
 */
public class Rect2D implements GeoShapeable {
	Point2D _p1, _p2, _p3, _p4;

	public Rect2D(Point2D p1, Point2D p2) {
		double minX = Math.min(p1.x(), p2.x()), maxX = Math.max(p1.x(), p2.x()), minY = Math.min(p1.y(), p2.y()), maxY = Math.max(p1.y(), p2.y());
		_p1 = new Point2D(minX, minY);
		_p2 = new Point2D(minX, maxY);
		_p4 = new Point2D(maxX, minY);
		_p3 = new Point2D(maxX, maxY);
	}
	public Rect2D(Rect2D orig) {
		_p1 = new Point2D(orig._p1);
		_p2 = new Point2D(orig._p2);
		_p3 = new Point2D(orig._p3);
		_p4 = new Point2D(orig._p4);
	}
	public Rect2D(String[] args) {
		if (args.length < 8) throw new IllegalArgumentException("Can't initialize Rect2D with less than 8 arguments");
		try { // We are going to make sure the quadrilateral is actually a rectangle
			// Get the points
			Point2D p1 = new Point2D(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			Point2D p2 = new Point2D(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
			Point2D p3 = new Point2D(Double.parseDouble(args[4]), Double.parseDouble(args[5]));
			Point2D p4 = new Point2D(Double.parseDouble(args[6]), Double.parseDouble(args[7]));
			// Get the slopes of the quadrilateral sides
			double p12Slope = (p1.y() - p2.y()) / (p1.x() - p2.x());
			double p43Slope = (p4.y() - p3.y()) / (p4.x() - p3.x());
			double p23Slope = (p2.y() - p3.y()) / (p2.x() - p3.x());
			double p14Slope = (p1.y() - p4.y()) / (p1.x() - p4.x());
			// Make sure the opposite slopes are close to equals - parallel
		 	if (Math.abs(p12Slope - p43Slope) >= Ex4_Const.EPS || Math.abs(p23Slope - p14Slope) >= Ex4_Const.EPS)
		 		throw new IllegalArgumentException("Cannot create non-parallelogram rectangle");
			// Make sure touching slopes are perpendicular, their multiplication is -1
		 	if ((Math.abs(p12Slope * p14Slope - -1) >= Ex4_Const.EPS)  // check if they are not perpendicular
			 && (p12Slope != 0 || Double.isFinite(p14Slope))           // also make sure they are not parallel to the axes
			 && (p14Slope != 0 || Double.isFinite(p12Slope))) {        // we check both options of axis-parallelity
				throw new IllegalArgumentException("Cannot create rectangle with non-right angle");
			}
			_p1 = p1; _p2 = p2; _p3 = p3; _p4 = p4;  // if all checks passed set the points
		} catch (IllegalArgumentException e) {   // if something failed, print the error and rethrow it
			System.err.println("ERROR: Could not initialize Rect2D: " + e.getMessage());
			throw e;
		}
    }

	@Override
	public String toString() {
		return "" + _p1 + ',' + _p2 + ',' + _p3 + ',' + _p4;
	}

	@Override
	public boolean contains(Point2D ot) {
		// This work under a very similar concept to Triangle2D.contains, only difference is that the distances to compare to are the side lengths
		double l12 = _p1.distance(_p2);
		double l23 = _p2.distance(_p3);
		double d12 = ot.distance(new Segment2D(_p1, _p2));
		double d23 = ot.distance(new Segment2D(_p2, _p3));
		double d34 = ot.distance(new Segment2D(_p3, _p4));
		double d14 = ot.distance(new Segment2D(_p1, _p4));
		boolean closerTo12 = d12 <= l23 + Ex4_Const.EPS;
		boolean closerTo23 = d23 <= l12 + Ex4_Const.EPS;
		boolean closerTo34 = d34 <= l23 + Ex4_Const.EPS;
		boolean closerTo14 = d14 <= l12 + Ex4_Const.EPS;
		return closerTo12 && closerTo23 && closerTo34 && closerTo14;
	}

	@Override
	public double area() {
		return toPoly().area();
	}

	@Override
	public double perimeter() {
		return toPoly().perimeter();
	}

	@Override
	public void move(Point2D vec) {
		_p1.move(vec);
		_p2.move(vec);
		_p3.move(vec);
		_p4.move(vec);
	}

	@Override
	public GeoShapeable copy() {
		return new Rect2D(this);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		_p1.scale(center, ratio);
		_p2.scale(center, ratio);
		_p3.scale(center, ratio);
		_p4.scale(center, ratio);
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		_p1.rotate(center, angleDegrees);
		_p2.rotate(center, angleDegrees);
		_p3.rotate(center, angleDegrees);
		_p4.rotate(center, angleDegrees);
	}

	@Override
	public Point2D[] getPoints() {
		List<Point2D> ps = Arrays.asList(getAllPoints());
		Comparator<Point2D> xComp = new Comparator<Point2D>() {
			@Override
			public int compare(Point2D p1, Point2D p2) {
				return Double.compare(p1.x(), p2.x());
			}
		};
		Comparator<Point2D> yComp = new Comparator<Point2D>() {
			@Override
			public int compare(Point2D p1, Point2D p2) {
				return Double.compare(p1.y(), p2.y());
			}
		};
		Double xMin = ((Point2D) Collections.min(ps, xComp)).x();
		Double xMax = ((Point2D) Collections.max(ps, xComp)).x();

		Double yMin = ((Point2D) Collections.min(ps, yComp)).y();
		Double yMax = ((Point2D) Collections.max(ps, yComp)).y();

		Point2D minP = new Point2D(xMin, yMin);
		Point2D maxP = new Point2D(xMax, yMax);

		return new Point2D[] { minP, maxP };
	}

	public Point2D[] getAllPoints() {
		return new Point2D[] { _p1, _p2, _p3, _p4 };
	}

	private Polygon2D toPoly() {
		return new Polygon2D(getAllPoints());
	}

	public boolean isSquare() {
		return Math.abs(_p1.distance(_p2) - _p2.distance(_p3)) < Ex4_Const.EPS;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Rect2D)) return false;
		Rect2D oRec = (Rect2D) o;
		Point2D[] ps = this.getAllPoints();
		Point2D[] ops = oRec.getAllPoints();
		for (int i = 0; i < ps.length; ++i) {
			if (!ps[i].closeToEquals(ops[i])) return false;
		}
		return true;
	}
}
