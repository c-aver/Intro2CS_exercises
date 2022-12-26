package Exe.Ex4.geo;

import java.util.ArrayList;

/**
 * This class represents a 2D polygon, as in https://en.wikipedia.org/wiki/Polygon
 * This polygon can be assumed to be simple in terms of area and contains.
 * 
 * You should update this class!
 * @author boaz.benmoshe
 *
 */
public class Polygon2D implements GeoShapeable{
	private ArrayList<Point2D> _points;   // TODO: this doesn't change often, array instead?

	public Polygon2D() {
		_points = new ArrayList<Point2D>();
	}
	public Polygon2D(ArrayList<Point2D> points) {
		_points = new ArrayList<Point2D>(points);
	}
	public void addPoint(Point2D p) {
		_points.add(p);
	}
	public void removePoint(Point2D p) {
		_points.remove(p);
	}
	
	@Override
	public boolean contains(Point2D ot) {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		return false;
	}

	@Override
	public double area() {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		return 0;
	}

	@Override
	public double perimeter() { // TODO: this function is untested
		double answer = 0;
		int numPoints = _points.size();
		for (int i = 0; i < numPoints; ++i) { // iterate on the points
			answer += _points.get(i).distance(_points.get((i + 1) % numPoints));  // add to the perimter the edge between current point and next one, wraping back to 0 for the last one
		}
		return answer;
	}

	@Override
	public void move(Point2D vec) {
		for (Point2D point : _points) {
			point.move(vec);
		}
	}

	@Override
	public GeoShapeable copy() {
		ArrayList<Point2D> copy = new ArrayList<Point2D>();
		for (Point2D point : _points) {
			copy.add(new Point2D(point));
		}
		return new Polygon2D(copy);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		for (Point2D point : _points) {
			point.scale(center, ratio);
		}
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		for (Point2D point : _points) {
			point.rotate(center, angleDegrees);
		}
	}

	@Override
	public Point2D[] getPoints() {
		return _points.toArray(new Point2D[0]);  // return the points arraylist converted to an array, the paramter is provided to determine the type of the array
	}
	
}
