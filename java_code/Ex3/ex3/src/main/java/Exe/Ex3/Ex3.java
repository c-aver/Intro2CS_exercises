package Exe.EX3;

import java.awt.Color;

/**
 * This class is a simple "inter-layer" connecting (aka simplifing) the
 * StdDraw_Ex3 with the Map2D interface.
 * Written for 101 java course it uses simple static functions to allow a 
 * "Singleton-like" implementation.
 * You should change this class!
 *
 */
public class Ex3 {
	private static Map2D _map = null;                        // the main logical representation of the map displayed on the screen
	private static Color _color = Color.black;               // the current brush color, default black
	private static String _mode = "";                        // the current brush mode, default none (anything that is not a known mode is none)
	private static Point2D _last = null;                     // the last clicked point, only used for 2-point brush modes (segment, rectangle, circle, shortest path)
	public static final int WHITE = Color.WHITE.getRGB();    // a default color
	public static final int BLACK = Color.BLACK.getRGB();    // another one

	public static void main(String[] args) {
		int dim = 10;
		init(dim);                  // init matrix (map) 10*10
	}
	private static void init(int x) {
		StdDraw_Ex3.clear();
		_map = new MyMap2D(x);
		StdDraw_Ex3.setScale(-0.5, _map.getHeight()-0.5);
		StdDraw_Ex3.enableDoubleBuffering();
		_map.fill(WHITE);
		drawArray(_map);		
	}
	
	public static void drawGrid(Map2D map) {
		 int w = map.getWidth();
		 int h = map.getHeight();
		 for(int i=0;i<w;i++) {
			 StdDraw_Ex3.line(i, 0, i, h);
		 }
		 for(int i=0;i<h;i++) {
			 StdDraw_Ex3.line(0, i, w, i);
		 }
	}
	static public void drawArray(Map2D a) {
		StdDraw_Ex3.clear();
		StdDraw_Ex3.setPenColor(Color.gray);
		drawGrid(_map);
		for(int y=0;y<a.getWidth();y++) {
			for(int x=0;x<a.getHeight();x++) {
				int c = a.getPixel(x, y);
				StdDraw_Ex3.setPenColor(new Color(c));
				drawPixel(x,y);
			}
		}		
		StdDraw_Ex3.show();
	}
	public static void actionPerformed(String p) {  // p is the selected option in the menues
		switch (p) {                       // switch p to perform the action associated with the selected option
			case "Clear":                  // option "Clear"
				_map.fill(WHITE);          // we fill the map with white
				break;                     // now we have to redraw the cleared map
			case "White":                  // color option "white"
				_color = Color.WHITE;      // change the brush color to white
				return;                    // in this case, we don't need to redraw the map, same thing for the next couple options
			case "Black":
				_color = Color.BLACK;
				return;
			case "Blue":
				_color = Color.BLUE; 
				return;
			case "Red":
				_color = Color.RED;  
				return;
			case "Yellow":
				_color = Color.YELLOW;
				return;
			case "Green":
				_color = Color.GREEN;
				return;
			case "20x20":                  // map option 20x20
			   init(20);                   // reinitialize the map with side length 20
			   break;                      // we have to redraw the new map, same thing for the next couple options
			case "40x40":
			   init(40); 
			   break;
			case "80x80":
			   init(80); 
			   break;
			case "160x160":
				init(160);
				break;
			default:                       // this any mode which is not clear, brush color, or map size
				_mode = p;                 // these are the brush modes, and they are represented in _mode
				return;                    // in these cases no need to redraw the map
		}
		drawArray(_map);                   // for options where we didn't return (clear and map size) we need to redraw the clear map
	}
	public static void mouseClicked(Point2D p) {  // p is the position of the mouse click
		System.out.println(p);                    // we print p to stdout for debug reasons (user should not be looking at console)
		int col = _color.getRGB();                // get the RGB value of the current brush color for drawing
		if(_mode.equals("Circle")) {              // if the current mode is circle drawing, this is the first click (center of the circle)
			_last = p;                            // we set the _last click as the current one
			_mode = "_Circle";                    // we set the mode to "_Circle", this symbolises the middle of drawing a circle
		}
		else if(_mode.equals("_Circle")) {  // if we are in the middle of drawing a circle (need else if to prevent instantly going here after changing mode)
			_map.drawCircle(_last, p.distance(_last), col);  // we draw a circle (in the current brush color) with the center at the last click and radius as the distance between last and current click (this puts current click on the circuference)
			_mode = "Circle";                     // we reset the mode to starting to draw a circle (in the original file this set the mode to none but I feel like my behavior is more intuitive for the user)
		}
		if(_mode.equals("Segment")) {             // if the mode is circle drawing, we are clicking the first point in the segment
			_last = p;                            // save the click location for the next click
			_mode = "_Segment";                   // set the mode to be in the middle of drawing a segment (similar to circle drawing)
		}
		else if(_mode.equals("_Segment")) {       // similarly to circle drawing, this symbolises the middle of drawing a segment
			_map.drawSegment(_last, p, col);      // we draw (in the current brush color) the segment from the last click to the current
			_mode = "Segment";                    // and reset to the start of segment drawing
		}
		if(_mode.equals("Rect")) {                // basically same thing as the last two
			_last = p;                            // yada yada
			_mode = "_Rect";                      // same old "_Draw"
		}
		else if(_mode.equals("_Rect")) {          // same thing
			_map.drawRect(p, _last, col);         // draw the rectangle between the current click and the last
			_mode = "Rect";                       // reset mode back
		}
		if(_mode.equals("Point")) {               // if the mode is point drawing
			_map.setPixel(p,col);                // we just need to set the specific clicked point to the brush color, no need to even pull this to a logical function
		}
		if(_mode.equals("Fill")) {                // if the mode is fill
			_map.fill(p, col);                    // we fill the map starting from the clicked point with the brush color (according to fill's logic)
		}
		if(_mode.equals("Gol")) {                 // if the mode is Game of Life
			_map.nextGenGol();	                  // we just need to compute the next generation, click location doesn't even matter
		}
		if(_mode.equals("ShortestPath")) {        // same thing as the 2D shape drawings
			_last = p;                            // remeber this click
			_mode = "_ShortestPath";              // set the mode to be in the middle of shortest path
		}
		else if(_mode.equals("_ShortestPath")) {  // if we just click the second point for the path
			Point2D p1 = new Point2D(_last.ix(), _last.iy()), p2 = new Point2D(p.ix(), p.iy());  // we round the two points to facilitate shortestPath (it acts weird with non-natural)
			Point2D[] path = _map.shortestPath(p1, p2);  // calculate the path between the points
			if (path != null)                            // if we actually found a path
				for (Point2D point : path)               // iterate on the points of the path
					_map.setPixel(point, col);           // and color them in the current brush color
			_mode = "ShortestPath";                      // reset mode
		} // TODO: make screen flash when no path was found
		System.out.println("New mode: " + _mode);        // for debug puposes (again, the user should not be looking at the console) print the new mode
		drawArray(_map);                                 // redraw the map after the changes
	}
	static private void drawPixel(int x, int y) {
		StdDraw_Ex3.filledCircle(x, y, 0.3);
	}
}