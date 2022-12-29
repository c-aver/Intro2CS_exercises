package Exe.Ex4.geo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a 2D rectangle (NOT necessarily axis parallel - this shape can be rotated!)
 * Ex4: you should implement this class!
 * @author I2CS
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
		try {
			Point2D p1 = new Point2D(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			Point2D p2 = new Point2D(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
			Point2D p3 = new Point2D(Double.parseDouble(args[4]), Double.parseDouble(args[5]));
			Point2D p4 = new Point2D(Double.parseDouble(args[6]), Double.parseDouble(args[7]));
			// TODO: this code might be buggy
			double p12Slope = (p1.y() - p2.y()) / (p1.x() - p2.x());
			double p43Slope = (p4.y() - p3.y()) / (p4.x() - p3.x());
			double p23Slope = (p2.y() - p3.y()) / (p2.x() - p3.x());
			double p14Slope = (p1.y() - p4.y()) / (p1.x() - p4.x());
		 	if (p12Slope != p43Slope || p23Slope != p14Slope)
		 		throw new IllegalArgumentException("Cannot create non-parallelogram rectangle");
		 	if ((p12Slope * p14Slope != -1)                          // check if they are not perpendicular
			 && (p12Slope != 0 || Double.isFinite(p14Slope))         // also make sure they are not parallel to the axes
			 && (p14Slope != 0 || Double.isFinite(p12Slope))) {      // we check both option of axis-parallelity
				throw new IllegalArgumentException("Cannot create rectangle with non-right angle");
			}
			_p1 = p1; _p2 = p2; _p3 = p3; _p4 = p4;
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
    }

	@Override
	public String toString() {
		return "Rect2D," + _p1 + ',' + _p2 + ',' + _p3 + ',' + _p4;
	}

	@Override
	public boolean contains(Point2D ot) {
		return toPoly().contains(ot);
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
	public Point2D[] getPoints() { // TODO: test this
		List<Point2D> ps = Arrays.asList(new Point2D[] { _p1, _p2, _p3, _p4});
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

	private Polygon2D toPoly() {
		return new Polygon2D(getPoints());
	}
}
