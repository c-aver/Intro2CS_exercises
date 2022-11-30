package Exe.EX3;
/**
 * This class implements the Map2D interface.
 * You should change (implement) this class as part of Ex3. */
public class MyMap2D implements Map2D{
	private int[][] _map;
	public static final int ALIVE = Ex3.BLACK;
	public static final int DEAD  = Ex3.WHITE;

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
		return fill(p.ix(), p.iy(), new_v);                  // send to the equivalent function with the coords as ints
	}

	@Override
	public int fill(int x, int y, int new_v) {
		int old_v = getPixel(x, y);                           // we need to remember what the old color was to correctly identify relevant neighbors
		if (old_v == new_v) return 0;                         // if we are already in the correct color we need to stop
		setPixel(x, y, new_v);                                // then we set our current pixel to the required color
		if ((x - 1) >= 0 && (getPixel(x - 1, y) == old_v))                   // if the point below and to the left is the same as the old color
			fill(x - 1, y, new_v);     // we fill it as well                    and it is within the borders
		if ((x + 1) < getWidth( )&& (getPixel(x + 1, y) == old_v))           // if the point above and to the left is the same as the old color
			fill(x + 1, y, new_v);     // we fill it as well                    and it is within the borders
		if ((y - 1) >= 0 && (getPixel(x, y - 1) == old_v))                   // if the point below and to the right is the same as the old color
			fill(x, y - 1, new_v);     // we fill it as well                    and it is within the borders
		if ((y + 1) < getHeight() && (getPixel(x, y + 1) == old_v))          // if the point below and to the left is the same as the old color
			fill(x, y + 1, new_v);     // we fill it as well                    and it is within the borders
		return 0;
	}

	@Override
	public Point2D[] shortestPath(Point2D p1, Point2D p2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int shortestPathDist(Point2D p1, Point2D p2) {
		if (p1.equals(p2)) return 0;                      // if the points are equal their distance is 0
		int ix = p1.ix(), iy = p1.iy();                   // get p1's coords as ints to find neighbors
		Point2D above = new Point2D(ix, iy + 1);  // get the point above p1
		Point2D below = new Point2D(ix, iy - 1);  // get the point below p1
		Point2D right = new Point2D(ix - 1, iy);  // get the point to the right of p1
		Point2D left  = new Point2D(ix + 1, iy);  // get the point to the left of p1
		int ans = Integer.MAX_VALUE;
		if (iy < getHeight() - 1 && getPixel(above) == getPixel(p1)) { // if the neighbor above is of the same color
			int dist = shortestPathDist(above, p2);                    // calculate the distance from it to p2
			if (dist < ans)                                            // if it is less than the current ans
				ans = dist;                                            // set the ans as it
		}
		if (iy > 0 && getPixel(below) == getPixel(p1)) {               // if the neighbor below is of the same color
			int dist = shortestPathDist(below, p2);                    // calculate the distance from it to p2
			if (dist < ans)                                            // if it is less than the current ans
				ans = dist;                                            // set the ans as it
		}
		if (ix < getWidth() - 1 && getPixel(right) == getPixel(p1)) {  // if the neighbor right is of the same color
			int dist = shortestPathDist(right, p2);                    // calculate the distance from it to p2
			if (dist < ans)                                            // if it is less than the current ans
				ans = dist;                                            // set the ans as it
		}
		if (ix > 0 && getPixel(left ) == getPixel(p1)) {               // if the neighbor left  is of the same color
			int dist = shortestPathDist(left , p2);                    // calculate the distance from it to p2
			if (dist < ans)                                            // if it is less than the current ans
				ans = dist;                                            // set the ans as it
		}
		return ans;
	}

	@Override
	public void nextGenGol() {
		int[][] ans = new int[getWidth()][getHeight()];                   // initialize answer matrix
		
		for (int x = 0; x < getWidth(); ++x)                              // iterate on matrix size
			for (int y = 0; y < getHeight(); ++y) {                       //="-
				ans[x][y] = DEAD;                                         // assume cell is going to be dead, set it as such
				int livingNeighbors = livingNeighbors(x, y);              // compute number of living neighbors
				if (isAlive(x, y)) {                                      // if the cell is alive
					if (livingNeighbors == 2 || livingNeighbors == 3)     // and it has 2 or 3 neighbors
						ans[x][y] = ALIVE;                                // it stays alive
				} else {                                                  // if the cell is dead
					if (livingNeighbors == 3)                             // and it has 3 neighbors
						ans[x][y] = ALIVE;                                // it comes to life
				}
			}

		for (int x = 0; x < getWidth(); ++x)                              // iterate on the map
			for (int y = 0; y < getHeight(); ++y)                         // -"-
				_map[x][y] = ans[x][y];                                   // copy the answer into the map
	}
	/**
	 * This functions decides whether a cell is alive for the purposes of GoL
	 * @param x x coordinate of the cell
	 * @param y y coordinate of the cell
	 * @return true iff the cell is alive
	 */
	protected boolean isAlive(int x, int y) {     // we need to check 3 things
		return    (x >= 0 && x < getWidth())      // x coord is within map
		       && (y >= 0 && y < getWidth())      // AND y coord is within map
			   && (getPixel(x, y) != DEAD);       // AND the cell is not dead (note that anything other than DEAD is considered alive, not just ALIVE)
	}
	/**
	 * This function computes the number of living neighbors of a cell for the puposes of GoL
	 * @param x x coordinate of the cell
	 * @param y y coordinate of the cell
	 * @return the number of living neighbors
	 */
	protected int livingNeighbors(int x, int y) {
		int res = 0;                                                   // initialize result as 0
		for (int i = -1; i <= 1; ++i)                                  // iterate on offsets of absolute value up to 1 on the x axis
			for (int j = -1; j <= 1; ++j)                              // iterate on offsets of absolute value up to 1 on the y axis
				if (((i != 0) || (j != 0)) && isAlive(x + i, y + j))   // if one of the offset is not 0 the cell is in fact a neighbor and not the current one, then if it is alive
					++res;                                             // increment the result
		return res;       // return the computed result
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
