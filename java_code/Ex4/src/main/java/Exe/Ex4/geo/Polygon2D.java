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
	private ArrayList<Point2D> _points;

	public Polygon2D() { //TODO: do we want this?
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
	public double perimeter() {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		return 0;
	}

	@Override
	public void move(Point2D vec) {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		
	}

	@Override
	public GeoShapeable copy() {
		ArrayList<Point2D> copy = new ArrayList<Point2D>();
		copy.addAll(_points);
		return new Polygon2D(copy);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		// TODO Auto-generated method stub
		assert false : "Polygon2D is not implemented";
		
	}

	@Override
	public Point2D[] getPoints() {
		return _points.toArray(new Point2D[0]);  // return the points arraylist converted to an array, the paramter is provided to determine the type of the array
	}
	
}
