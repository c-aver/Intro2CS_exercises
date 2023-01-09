/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4.geo;

/**
 * This class represents a 2D polygon, as in https://en.wikipedia.org/wiki/Polygon
 * This polygon can be assumed to be simple in terms of area and contains.
 * 
 * You should update this class!
 * @author c-aver
 *
 */
public class Polygon2D implements GeoShapeable {
	private Point2D[] _points;
	private Triangle2D[] _mesh = null;  // this is a triangle mesh representing the polygon, singelton-like

	private Triangle2D[] getMesh() {  // this calculates the mesh
		if (_mesh != null) return _mesh;
		return _mesh = triangleMesh();
	}

	public Polygon2D(Point2D[] points) {
		_points = points;
		if (_points.length > 2)
			_mesh = triangleMesh();
	}
	public Polygon2D(String[] args) {
		if (args.length % 2 != 0) throw new IllegalArgumentException("Can't initialize Polygon2D with an odd number of arguments");
		_points = new Point2D[args.length / 2];
		for (int i = 0; i < _points.length; ++i) {
			String[] pointArgs = new String[] { args[i*2], args[i*2+1] };
			_points[i] = new Point2D(pointArgs);
		}
		if (_points.length > 2)
			_mesh = triangleMesh();
	}

	private void regenMesh() {  // this recalculates the mesh after changing the polygon
		_mesh = triangleMesh();
	}

	@Override
	public String toString() {
		String res = _points[0].toString();
		for (int i = 1; i < _points.length; ++i) {
			res += "," + _points[i];
		}
		return res;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Polygon2D)) return false;
		Polygon2D op = (Polygon2D) o;
		for (int i = 0; i < _points.length; ++i) {
			if (!_points[i].closeToEquals(op.getPoints()[i])) return false;
		}
		return true;
	}
	
	/**
	 * This function uses the triangle mesh to check if a point is contained in the polygon.
	 * The point is contained if it is contained in an odd number of triangle from the mesh.
	 * (Each triangle the point meets either "moves it" into or out of the polygon.)
	 */
	@Override
	public boolean contains(Point2D ot) {  // TODO: wrong when point is right on mesh borders
		int hits = 0;
		for (Triangle2D tri : getMesh()) {
			if (tri.contains(ot)) hits += 1;
		}
		return hits % 2 == 1;
	}

	@Override
	public double area() {
		// this uses the method described in https://en.wikipedia.org/wiki/Polygon#Simple_polygons
		double res = 0;
		for (int i = 0; i < _points.length; ++i) {
			res += _points[i].x()*_points[(i+1) % _points.length].y() - _points[(i+1) % _points.length].x()*_points[i].y();
		}
		return Math.abs(res) / 2;
	}

	@Override
	public double perimeter() {
		double answer = 0;
		int numPoints = _points.length;
		for (int i = 0; i < numPoints; ++i) { // iterate on the points
			answer += _points[i].distance(_points[(i + 1) % numPoints]);  // add to the perimter the edge between current point and next one, wraping back to 0 for the last one
		}
		return answer;
	}

	@Override
	public void move(Point2D vec) {
		for (Point2D point : _points) {
			point.move(vec);
		}
		regenMesh();
	}

	@Override
	public GeoShapeable copy() {
		Point2D[] copy = new Point2D[_points.length];
		for (int i = 0; i < _points.length; ++i) {
			copy[i] = (Point2D) _points[i].copy();
		}
		return new Polygon2D(copy);
	}

	@Override
	public void scale(Point2D center, double ratio) {
		for (Point2D point : _points) {
			point.scale(center, ratio);
		}
		regenMesh();
	}

	@Override
	public void rotate(Point2D center, double angleDegrees) {
		for (Point2D point : _points) {
			point.rotate(center, angleDegrees);
		}
		regenMesh();
	}

	@Override
	public Point2D[] getPoints() {
		return _points;  // return the points arraylist converted to an array, the paramter is provided to determine the type of the array
	}
	/**
	 * This function creates a triangle mesh of the polygon.
	 * Note that for concave polygon we can have intersections (due to the method used)
	 * But 
	 * @return an array of triangle representing the mesh
	 */
	private Triangle2D[] triangleMesh() {
		assert _points.length > 2;     // make sure we have at least a triangle, a segment can't be meshed
		int numTriangles = _points.length - 2;   // we have a triangle for each vertex after the first two
		Triangle2D[] result = new Triangle2D[numTriangles];
		Point2D origin = _points[_points.length - 1]; // get the last point as the "origin" for each triangle
		for (int i = 0; i < numTriangles; ++i) {
			// create a triangle between the origin, current point, and next point, on the last iteration next point will be the one before the origin
			result[i] = new Triangle2D(origin, _points[i], _points[i + 1]);
		}
		return result;
	}
}
