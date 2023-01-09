/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4.geo;

/**
 * This class represents a 2D segment on the plane, 
 * Ex4: you should implement this class!
 * @author c-aver
 *
 */
public class Segment2D implements GeoShapeable {
	public static final double SEGMENT_WIDTH = 0.1;  // this represents a practical segment width for the purpose of containment

	private Point2D _p1;   // the endpoints of the segment
	private Point2D _p2;

	public Segment2D(Point2D p1, Point2D p2) {
		_p1 = new Point2D(p1); _p2 = new Point2D(p2);
	}
	public Segment2D(double x1, double y1, double x2, double y2) {
		_p1 = new Point2D(x1, y1); _p2 = new Point2D(x2, y2);
	}
	public Segment2D(Segment2D seg) {
		this(seg.getPoints()[0], seg.getPoints()[1]);
	}
	public Segment2D(String[] args) {
		if (args.length < 4) throw new IllegalArgumentException("Can't initialize Segment2D with less than 4 arguments");
		try {
			_p1 = new Point2D(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
			_p2 = new Point2D(Double.parseDouble(args[2]), Double.parseDouble(args[3]));
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: Could not initialize Segment2D: " + e.getMessage());
		}
	}
	@Override
	public String toString() {
		return "" + _p1 + "," + _p2;
	}

	@Override
	public boolean contains(Point2D ot) {
		double minX = Math.min(_p1.x(), _p2.x()), maxX = Math.max(_p1.x(), _p2.x()), minY = Math.min(_p1.y(), _p2.y()), maxY = Math.max(_p1.y(), _p2.y());
		if (ot.x() < minX || ot.x() > maxX || ot.y() < minY || ot.y() > maxY) return false;
		if (ot.distance(this) > SEGMENT_WIDTH) return false;
		return true;
	}

	@Override
	public double area() {
		return 0;
	}

	@Override
	public double perimeter() {
		return _p1.distance(_p2) * 2;      // the perimeter is twice the length of the segment, which is the distance between the points
	}

	@Override
	public void move(Point2D vec) {
		_p1.move(vec);   // move each of the points by the vector
		_p2.move(vec);
	}

	@Override
	public GeoShapeable copy() {
		return new Segment2D(this);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		_p1.scale(center, ratio);   // scale each of the points by the parameters
		_p2.scale(center, ratio);
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		_p1.rotate(center, angleDegrees);   // rotate each of the points by the parameters
		_p2.rotate(center, angleDegrees);
	}

	@Override
	public Point2D[] getPoints() {
		return new Point2D[] { new Point2D(_p1), new Point2D(_p2) };
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Segment2D)) return false;
		Segment2D os = (Segment2D) o;
		return os.getPoints()[0].equals(_p1) && os.getPoints()[1].equals(_p2);
	}
}