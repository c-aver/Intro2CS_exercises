package Exe.EX3;
/**
 * This class implements the Map2D interface.
 * You should change (implement) this class as part of Ex3. */
public class MyMap2D implements Map2D{
	private int[][] _map;
	
	public MyMap2D(int w, int h) {init(w,h);}
	public MyMap2D(int size) {this(size,size);}
	public MyMap2D(int[][] data) { 
		this(data.length, data[0].length);
		init(data);
	}

	@Override
	public void init(int w, int h) {
		_map = new int[w][h];
	}
	@Override
	public void init(int[][] arr) {
		init(arr.length,arr[0].length);
		for(int x = 0; x < this.getWidth() &&  x < arr.length; x++) {
			for(int y = 0; y < this.getHeight() && y < arr[0].length; y++) {
				this.setPixel(x, y, arr[x][y]);
			}
		}
	}
	
	@Override
	public int getWidth() { return _map.length; }
	@Override
	public int getHeight() { return _map[0].length; }
	@Override
	public int getPixel(int x, int y) { return _map[x][y]; }
	@Override
	public int getPixel(Point2D p) { 
		return this.getPixel(p.ix(),p.iy());
	}
	
	public void setPixel(int x, int y, int v) { _map[x][y] = v; }
	public void setPixel(Point2D p, int v) { 
		setPixel(p.ix(), p.iy(), v);
	}

	@Override
	public void drawSegment(Point2D p1, Point2D p2, int v) {
		// TODO Auto-generated method stub
        
	}

	@Override
	public void drawRect(Point2D p1, Point2D p2, int col) {
		int x1 = p1.ix(), x2 = p2.ix();                     // pull x coords from points as ints
		int y1 = p1.iy(), y2 = p2.iy();                     // pull y coords from points as ints
		if (x1 > x2) { int t = x1; x1 = x2; x2 = t; }       // make sure x1 is the smaller
		if (y1 > y2) { int t = y1; y1 = y2; y2 = t; }       // make sure y1 is the smaller
		for (int x = x1; x <= x2; ++x)                      // iterate on all points with x1 <= x <= x2
			for (int y = y1; y <= y2; ++y)                  // iterate on all points with y1 <= y <= y2
				setPixel(x, y, col);                        // set the point to the requested color
	}

	@Override
	public void drawCircle(Point2D p, double rad, int col) {
		int x1 = p.ix() - (int) rad, x2 = p.ix() + (int) rad;   // set the furtherstmost possible x values
		int y1 = p.iy() - (int) rad, y2 = p.iy() + (int) rad;   // set the furtherstmost possible y values
		for (int x = x1; x <= x2; ++x)                          // iterate on all points with x1 <= x <= x2
			for (int y = y1; y <= y2; ++y) {                    // iterate on all points with y1 <= y <= y2
				double dist = p.distance(new Point2D(x, y));    // compute the distance between the current point and the center
				if ((dist * dist) < (rad * rad))                // if the squared distance is less than the squared radius, we are inside the circle
					setPixel(x, y, col);                        // so we set the pixel to the required color
			}
	}

	@Override
	public int fill(Point2D p, int new_v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fill(int x, int y, int new_v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point2D[] shortestPath(Point2D p1, Point2D p2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int shortestPathDist(Point2D p1, Point2D p2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void nextGenGol() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void fill(int c) {
		for(int x = 0; x < this.getWidth(); x++) {
			for(int y = 0; y < this.getHeight(); y++) {
				this.setPixel(x, y, c);
			}
		}
		
	}

}
