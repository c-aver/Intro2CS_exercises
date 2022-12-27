package Exe.Ex4.geo;

import java.util.ArrayList;

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
		_p1 = orig._p1;
		_p2 = orig._p2;
		_p3 = orig._p3;
		_p4 = orig._p4;
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
	public Point2D[] getPoints() {
		return new Point2D[] { _p1, _p2, _p3, _p4 };
	}

	private Polygon2D toPoly() {
		ArrayList<Point2D> list = new ArrayList<Point2D>();
		list.add(_p1);
		list.add(_p2);
		list.add(_p3);
		list.add(_p4);
		return new Polygon2D(list);
	}
}
