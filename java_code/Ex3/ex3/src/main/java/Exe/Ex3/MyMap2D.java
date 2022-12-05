package Exe.EX3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements the Map2D interface.
 * You should change (implement) this class as part of Ex3. */
public class MyMap2D implements Map2D {
	private int[][] _map;

	public static final int BACKGROUND = Ex3.WHITE;
	public static final int ALIVE = Ex3.BLACK;
	public static final int DEAD  = Ex3.WHITE;

	public MyMap2D(int w, int h) {init(w,h);}
	public MyMap2D(int size) {this(size,size);}
	public MyMap2D(int[][] data) { 
		this(data.length, data[0].length);
		init(data);
	}
	public MyMap2D(MyMap2D original) {                   // copy constructor
		this(original.getWidth(), original.getHeight());
		init(original._map);
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
	public int getPixel(int x, int y) {
		if (inBounds(x, y)) return _map[x][y];   // make sure the coordinates are within the map
		else return BACKGROUND;                  // otherwise we assume background color
	}
	@Override
	public int getPixel(Point2D p) { 
		return this.getPixel(p.ix(),p.iy());
	}
	
	public void setPixel(int x, int y, int v) { if (inBounds(x, y)) _map[x][y] = v; } // only set the pixel if it is within the map
	public void setPixel(Point2D p, int v) { 
		setPixel(p.ix(), p.iy(), v);
	}

	/**
	 * This function determines whether a given point is within the boundries of the map
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return true iff the point is within the boundries of the map
	 */
	private boolean inBounds(int x, int y) {
		return (x >= 0 && x < getWidth())         // x coord is within map
		    && (y >= 0 && y < getWidth());        // AND y coord is within map
	}
	/**
	 * This function determines whether a given point is within the boundries of the map
	 * @param p the point to be checked
	 * @return true iff the point is within the boundries of the map
	 */
	private boolean inBounds(Point2D p) {
		return inBounds(p.ix(), p.iy());
	}

	@Override
	public void drawSegment(Point2D p1, Point2D p2, int v) { // TODO: can create elbows TODO: 4, 5 creates elbow
		Point2D ip1 = new Point2D(p1.ix(), p1.iy()), ip2 = new Point2D(p2.ix(), p2.iy());
		double dx = ip2.x() - ip1.x(), dy = ip2.y() - ip1.y();   // find the delta in the axes
		double dist = Math.sqrt(dx*dx + dy*dy);                  // find the distance between the points
		double xStep = dx / dist, yStep = dy / dist;             // calulate the required step distances by some expression I made up that seems to work fine
		Point2D step = new Point2D(xStep, yStep);                // create a step vector
		double stepDist = step.distance();                       // calculate the distance of the step vector, hlaf of this is the closest we will ever get to the target
		Point2D cursor = new Point2D(ip1);                       // start the cursor on p1
		while (!cursor.close2equals(ip2, stepDist / 2)) {        // run the cursor through the segment as long as we are not within p2
			setPixel(cursor, v);                                 // set the pixel under the cursor to the required color
			cursor = cursor.add(step);                           // step the cursor by the step vector
		}    
		setPixel(ip2, v);                                        // now we just need to set the target point (we close enough to reached it)
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
		int x1 = p.ix() - (int) rad, x2 = p.ix() + (int) rad;   // set the fartherstmost possible x values
		int y1 = p.iy() - (int) rad, y2 = p.iy() + (int) rad;   // set the fartherstmost possible y values
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
		int ans = 0;                                    // initialize answer as 0

		boolean[][] visited = new boolean[getWidth()][getHeight()];  // matrix to track whether a pixel was visited
		Queue<Point2D> q = new LinkedList<Point2D>();                // the queue of points to check through
		q.add(new Point2D(x, y));                                      // add the origin point as the first point to check
		visited[x][y] = true;               // document that the origin was visited (so we don't step back into it)
		while (!q.isEmpty()) {                          // iterate as long as we have points to color
			Point2D next = q.remove();                  // take the next point to be colored
			LinkedList<Point2D> legalNeighbors = legalNeighbors(next, visited); // get the list of legal neighbors of the current point, in respect to the previously visited points, have to do this before coloring it to determine if they are the same color
			setPixel(next, new_v);                      // color the current point in the required color
			ans += 1;
			q.addAll(legalNeighbors);                   // add the legal neighbors to the queue to be processed
			for (Point2D neighbor : legalNeighbors)           // iterate on the neighbors
				visited[neighbor.ix()][neighbor.iy()] = true; // document that they have been visited and put into the queue so the next neighbor doesn't have to
		}
		return ans;
	}
	
	/**
	 * This function checks whether a neighbor is legal to make a path through
	 * @param p1 the current point
	 * @param p2 the neighbor
	 * @param visited an array stating whether a point was visited
	 * @return whether the neighbor is legal to go through
	 */
	private boolean isLegal(Point2D p1, Point2D p2, boolean[][] visited) {
		int x = p2.ix(), y = p2.iy();             // get p2's coords for ease of use
		                                          // if we didn't return already we need to check a couple things:
		return inBounds(p2)                       // make sure neighbor is within bounds, to avoid out of bounds errors on the next conditions
			&& (!visited[x][y])                   // the neighbor was not already visited
			&& (getPixel(p1) == getPixel(p2));    // AND p2 is of the same color as p1
	}
	/**
	 * This function return a list of all legal neighbors of a point
	 * @param p point to check the neighbors of
	 * @param visited an array stating whether a point was visited
	 * @return a list of all legal neighbors
	 */
	private LinkedList<Point2D> legalNeighbors(Point2D p, boolean[][] visited) {
		LinkedList<Point2D> ans = new LinkedList<>();   // initialize result list
		int x = p.ix(), y = p.iy();                     // get p1's coords as ints to find neighbors
		Point2D above = new Point2D(x, y + 1);          // get the point above p1
		Point2D below = new Point2D(x, y - 1);          // get the point below p1
		Point2D right = new Point2D(x - 1, y);          // get the point to the right of p1
		Point2D left  = new Point2D(x + 1, y);          // get the point to the left of p1
		if (isLegal(p, above, visited)) ans.add(above); // if neighbor above is legal, add it to result list
		if (isLegal(p, below, visited)) ans.add(below); // if neighbor below is legal, add it to result list
		if (isLegal(p, right, visited)) ans.add(right); // if neighbor to the right is legal, add it to result list
		if (isLegal(p, left , visited)) ans.add(left ); // if neighbor to the left is legal, add it to result list
		return ans;                                     // return result list
	}
	@Override
	public Point2D[] shortestPath(Point2D p1, Point2D p2) {
		int dist = 0;                                    // initialize answer as 0
		int currentDistCount = 1;                       // initialize count of pixels in the current distance as 1 (the origin we are about to add)
		int nextDistCount = 0;                          // initialize count of pixels in the next distance as 0
		boolean foundPath = false;                      // assume we are not going to find a path

		boolean[][] visited = new boolean[getWidth()][getHeight()];  // matrix to track whether a pixel was visited
		Point2D[][] parent = new Point2D[getWidth()][getHeight()];   // matrix to track which point led to each of the visited one, to reconstruct the path
		Queue<Point2D> q = new LinkedList<Point2D>();                // the queue of points to check through
		q.add(p1);                                      // add the origin point as the first point to check
		visited[p1.ix()][p1.iy()] = true;               // document that the origin was visited (so we don't step back into it)
		while (!q.isEmpty()) {                          // iterate as long as we have points to check
			Point2D next = q.remove();                  // take the next point to be processed
			--currentDistCount;                         // document the fact that the current distance from the origin has one less point in the queue
			if (next.equals(p2)) {                      // if the point is the destination
				foundPath = true;                       // document that we found a path
				break;                                  // exit the loop to start reconstructing the path
			}
			LinkedList<Point2D> legalNeighbors = legalNeighbors(next, visited); // get the list of legal neighbors of the current point, considering the previously visited points
			nextDistCount += legalNeighbors.size();     // add to the next distance count the number of legal neighbors (they are 1 farther away than the current point)
			q.addAll(legalNeighbors);                   // add the legal neighbors to the queue to be processed after the current distance is finished
			for (Point2D neighbor : legalNeighbors) {          // iterate on the legal neighbors of the currently processing point
				visited[neighbor.ix()][neighbor.iy()] = true;  // document they have been visited
				parent[neighbor.ix()][neighbor.iy()] = next;   // set the current as their parent
			}
			if (currentDistCount == 0) {                // if we ran out of points in the current distance, we are going to start processing the next distance
				++dist;                                 // since we are now on the next distance, if we find the destination it is one farther away than before
				currentDistCount = nextDistCount;       // move the next distance to the current distance, to start decrementing as we process the points
				nextDistCount = 0;                      // set the next distance count to be 0, to start accumulating as we add neighbors
			}
		}
		if (!foundPath) return null;                // if we finished processing all points and never found the destination, return null to signal no path found
		Point2D[] path = new Point2D[dist + 1];     // initialize the array path one longer than the distance (since it is inclusive of origin and destination)
		Point2D current = p2;                       // the first point in the path is the destination (it is reversed but that's meaningless)
		for (int i = 0; i < path.length; ++i) {    // iterate on the elements of the path
			path[i] = current;                      // set the point in the path to the current point
			current = parent[current.ix()][current.iy()]; // set the next current point to the current's parent (to step back in the path)
		}
		return path;
	}
	@Override
	public int shortestPathDist(Point2D p1, Point2D p2) {
		int ans = 0;                                    // initialize answer as 0
		int currentDistCount = 1;                       // initialize count of pixels in the current distance as 1 (the origin we are about to add)
		int nextDistCount = 0;                          // initialize count of pixels in the next distance as 0

		boolean[][] visited = new boolean[getWidth()][getHeight()];  // matrix to track whether a pixel was visited
		Queue<Point2D> q = new LinkedList<Point2D>();                // the queue of points to check through
		q.add(p1);                                      // add the origin point as the first point to check
		visited[p1.ix()][p1.iy()] = true;               // document that the origin was visited (so we don't step back into it)
		while (!q.isEmpty()) {                          // iterate as long as we have points to check
			Point2D next = q.remove();                  // take the next point to be processed
			--currentDistCount;                         // document the fact that the current distance from the origin has one less point in the queue
			if (next.equals(p2)) return ans;            // if the point is the destination, return the current distance
			LinkedList<Point2D> legalNeighbors = legalNeighbors(next, visited); // get the list of legal neighbors of the current point, in respect to the previously visited points
			nextDistCount += legalNeighbors.size();     // add to the next distance count the number of legal neighbors (they are 1 farther away than the current point)
			q.addAll(legalNeighbors);                   // add the legal neighbors to the queue to be processed after the current distance is finished
			for (Point2D neighbor : legalNeighbors)           // iterate on the neighbors
				visited[neighbor.ix()][neighbor.iy()] = true; // document that they have been visited and put into the queue so the next neighbor doesn't have to
			if (currentDistCount == 0) {                // if we ran out of points in the current distance, we are going to start processing the next distance
				++ans;                                  // since we are now on the next distance, if we find the destination it is one farther away than before
				currentDistCount = nextDistCount;       // move the next distance to the current distance, to start decrementing as we process the points
				nextDistCount = 0;                      // set the next distance count to be 0, to start accumulating as we add neighbors
			}
		}
		return -1;      // if we finished processing all points and never found the destination, return -1 to signal no path found
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
	 * This function computes the number of living neighbors of a cell for the purposes of GoL
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
	public void nextGenGol() {
		int[][] ans = new int[getWidth()][getHeight()];                   // initialize answer matrix
		
		for (int x = 0; x < getWidth(); ++x)                              // iterate on matrix size
			for (int y = 0; y < getHeight(); ++y) {                       // -"-
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

	@Override
	public void fill(int c) {
		for(int x = 0; x < this.getWidth(); x++) {           // iterate on the map
			for(int y = 0; y < this.getHeight(); y++) {      // -"-
				this.setPixel(x, y, c);                      // set the pixel to the required color
			}
		}
	}
}