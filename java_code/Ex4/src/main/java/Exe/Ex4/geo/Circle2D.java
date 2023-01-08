/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

import Exe.Ex4.Ex4_Const;

/** 
 * This class represents a 2D circle in the plane. 
 * Please make sure you update it according to the GeoShape interface.
 * Ex4: you should update this class!
 * @author c-aver
 *
 */
public class Circle2D implements GeoShapeable {
	private Point2D _center;
	private double _radius;
	
	public Circle2D(Point2D cen, double rad) {
		this._center = new Point2D(cen);
		this._radius = rad;
	}
	public Circle2D(String[] args) {
		try {
			_center = new Point2D(args[0] + "," + args[1]);
			_radius = Double.parseDouble(args[2]);
		} catch (IllegalArgumentException e) {
			System.err.println("ERROR: Could not initialize Circle2D: " + e.getMessage());
		}
	}
	public double getRadius() { return this._radius; }
	@Override
	public String toString() {
		return "" + _center + ", " + _radius;
	}
	@Override
	public boolean contains(Point2D ot) {
		double dist = ot.distance(this._center);
		return dist <= this._radius;
	}
	
	@Override
	public double area() {
		return Math.PI * Math.pow(this._radius, 2);
	}
	@Override
	public double perimeter() {
		return Math.PI * 2 * this._radius;
	}
	@Override
	public void move(Point2D vec) {
		_center.move(vec);
	}
	@Override
	public GeoShapeable copy() {
		return new Circle2D(_center, _radius);
	}
	@Override
	public Point2D[] getPoints() {
		Point2D[] ans = new Point2D[2];
		ans[0] = new Point2D(this._center);
		ans[1] = new Point2D(ans[0].x(), ans[0].y()+this._radius);
		return ans;
	}
	@Override
	public void scale(Point2D center, double ratio) {
		_center.scale(center, ratio);    // scale (actually move, since points have no area) the center
		_radius = Math.abs(_radius * ratio);  // we take the absolute value to allow negative ratios, although not strictly required
	}
	@Override
	public void rotate(Point2D center, double angleDegrees) {
		_center.rotate(center, angleDegrees);  // rotate the center point, radius does not change
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Circle2D)) return false;
		Circle2D oCirc = (Circle2D) o;
		if (Math.abs(oCirc.getRadius() - this.getRadius()) > Ex4_Const.EPS) return false;
		return oCirc.getPoints()[0].closeToEquals(_center);
	}
}
