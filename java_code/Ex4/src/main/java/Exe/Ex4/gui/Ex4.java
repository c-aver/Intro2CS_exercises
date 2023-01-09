/**
 * Name: Chaim Averbach
 * ID: 207486473
 */
package Exe.Ex4.gui;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.FileDialog;
import java.awt.Frame;
import java.util.ArrayList;

import Exe.Ex4.Ex4_Const;
import Exe.Ex4.GUIShape;
import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionable;
import Exe.Ex4.geo.Circle2D;
import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Polygon2D;
import Exe.Ex4.geo.Rect2D;
import Exe.Ex4.geo.Segment2D;
import Exe.Ex4.geo.ShapeComp;
import Exe.Ex4.geo.Triangle2D;

/**
 * 
 * This class is a simple "inter-layer" connecting (aka simplifying) the
 * StdDraw with the Map class.
 * Written for 101 java course it uses simple static functions to allow a 
 * "Singleton-like" implementation.
 * @author c-aver
 *
 */
public class Ex4 implements Ex4_GUI {
	private final boolean DEBUG = false;  // debug mode
	private enum FileDialogMode { LOAD, SAVE }

	private ShapeCollectionable _shapes = new ShapeCollection();  // the shapes in the canvas
	private GUI_Shapeable _previewShape = new GUIShape(null, false, Color.pink, 0);    // the shape currently being drawn, the only field that should be changed is its GeoShapeable
	private ArrayList<Point2D> _polyPoints = null;  // the list of points in the polygon being drawn
	private Color _color = Color.blue;   // the current brush color
	private boolean _fill = false;       // whether we are creating a filled shape
	private String _mode = "";           // the current brush mode
	private Point2D _lastClick = null;   // the last click location this should be null if and only if we are not currently drawing a shape
	private int _runningTag = 1;         // this is the tag to be provided for the next added shape, make sure to increment it when you add a shape, starts as 1 since 0 is reserved for preview
	
	private static Ex4 _winEx4 = null;
	
	private Ex4() {
		init(null);
	}
	public void init(ShapeCollectionable s) {
		if (s == null) { _shapes = new ShapeCollection(); }
		else { _shapes = s.copy(); }
		_color = Color.blue;
		_fill = false;
		_mode = "";
		if (s != null) _runningTag = s.size() + 1;  // if we are initializing with a collection set the running tag accordingly
	}
	public void show(double d) {
		StdDraw_Ex4.setScale(0, d);
		StdDraw_Ex4.show();
		drawShapes();
	}
	public static Ex4 getInstance() {
		if(_winEx4 == null) {
			_winEx4 = new Ex4();
		}
		return _winEx4;
	}
	
	private void drawShapes() {
		StdDraw_Ex4.clear();   // clear the screen to prepare for new shapes
		for (int i = 0; i < _shapes.size(); ++i) {  // iterate on the shapes
			GUI_Shapeable sh = _shapes.get(i);
			drawShape(sh);            // draw each one
		}
		if (_previewShape.getShape() != null) { drawShape(_previewShape); }   // if the preview shape is not null, draw it as well (on top)
		StdDraw_Ex4.show();   // show the new buffer
	}
	private static void drawShape(GUI_Shapeable g) {
		StdDraw_Ex4.setPenColor(g.getColor());                             // set the pen color to the shape's color
		if (g.isSelected()) { StdDraw_Ex4.setPenColor(Color.gray); }       // if it selected we draw it in gray
		GeoShapeable gs = g.getShape();                // get the GeoShapeable inside
		boolean isFill = g.isFilled();                 // check whether it should be filled
		if (gs instanceof Circle2D) {                  // if the shape is a circle
			Circle2D circle = (Circle2D) gs;           // cast it as such
			Point2D center = circle.getPoints()[0];    // get the center
			double rad = circle.getRadius();           // get the radius
			if (isFill) {                              // draw it with the parameters, determining which function to use on isFille
				StdDraw_Ex4.filledCircle(center.x(), center.y(), rad);
			} else { 
				StdDraw_Ex4.circle(center.x(), center.y(), rad);
			}
		}
		if (gs instanceof Segment2D) {   // if it is a segment
			Point2D[] ps = gs.getPoints();   // get the points
			StdDraw_Ex4.line(ps[0].x(), ps[0].y(), ps[1].x(), ps[1].y());  // draw the segement
		}
		if (gs instanceof Polygon2D || gs instanceof Triangle2D || gs instanceof Rect2D) {    // all these shapes are treated the same way
			Point2D[] ps = gs.getPoints();                                        // get the points
			if (gs instanceof Rect2D) { ps = ((Rect2D) gs).getAllPoints(); }      // Rect2D specifically need all points, since getPoints only returns bounding box
			double[] xs = new double[ps.length], ys = new double[ps.length];      // create two arrays for the coordinates
			for (int i = 0; i < ps.length; ++i) {                                 // copy the points' coordinates into the arrays
				xs[i] = ps[i].x();
				ys[i] = ps[i].y();
			}
			if (isFill) {     // draw the polygon, depending on isFill
				StdDraw_Ex4.filledPolygon(xs, ys);
			} else {
				StdDraw_Ex4.polygon(xs, ys);
			}
		}
	}
	private void updateColor(Color c) {   // the next two functions update the status of selected shapes with either a color or a fill state
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			if (s.isSelected()) {
				s.setColor(c);
			}
		}
	}
	private void updateFill() {
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			if (s.isSelected()) {
				s.setFilled(_fill);
			}
		}
	}

	public void actionPerformed(String action) {
		_mode = action;
		_lastClick = null;      // nullify last click when mode changes since next click is the first

		// File menu
		if (action.equals("Clear"))  { _shapes.removeAll(); _runningTag = 1; } // if the option was clear, remove all shapes from the canvas and reset the running tag to 1
		if (action.equals("Save")) {                         // on save (or load) call chooseFile to show the dialog (with appropriate constant to select mode), and if not null call the appropriate function
			String filePath = chooseFile(FileDialogMode.SAVE);
			if (filePath != null) _shapes.save(filePath);
		}
		if (action.equals("Load")) {
			String filePath = chooseFile(FileDialogMode.LOAD);
			if (filePath != null) _shapes.load(filePath);
		}

		// Select menu
		if (action.equals("All")) {
			for (int i = 0; i < _shapes.size(); ++i) {  // iterate on all shapes
				_shapes.get(i).setSelected(true);       // set selected to true
			}
		}
		if (action.equals("Anti")) {
			for (int i = 0; i < _shapes.size(); ++i) {  // iterate on all shapes
				GUI_Shapeable shape = _shapes.get(i);   // get the shape at the index
				shape.setSelected(!shape.isSelected()); // set its selected field to the inverse of the current value
			}
		}
		if (action.equals("None")) {
			for (int i = 0; i < _shapes.size(); ++i) {  // iterate on all shapes
				_shapes.get(i).setSelected(false);      // set selected to false
			}
		}
		if (action.equals("Info")) {
			for (int i = 0; i < _shapes.size(); ++i) {  // iterate on the shapes
				GUI_Shapeable shape = _shapes.get(i);
				System.out.println(shape);              // print the shape as a string
			}
		}

		// Color menu
		if (action.equals("Blue"))   { _color = Color.BLUE;   updateColor(_color); }  // if the option was a color, set the brush color to it and update selected shapes' color
		if (action.equals("Red"))    { _color = Color.RED;    updateColor(_color); }
		if (action.equals("Green"))  { _color = Color.GREEN;  updateColor(_color); }
		if (action.equals("White"))  { _color = Color.WHITE;  updateColor(_color); }
		if (action.equals("Black"))  { _color = Color.BLACK;  updateColor(_color); }
		if (action.equals("Yellow")) { _color = Color.YELLOW; updateColor(_color); }
		if (action.equals("Fill"))   { _fill = true;  updateFill(); }         // if the option was a filling option, set the brush filling and update selected
		if (action.equals("Empty"))  { _fill = false; updateFill(); }
		
		// Edit menu
		if (action.equals("Remove")) { removeSelected(); }

		// Sort menu
		// simply sorts the shapes by the relevant comparator
		if (action.equals("ByArea")) { _shapes.sort(ShapeComp.CompByArea); }
		if (action.equals("ByAntiArea")) { _shapes.sort(ShapeComp.CompByAntiArea); }
		if (action.equals("ByPerimeter")) { _shapes.sort(ShapeComp.CompByPerimter); }
		if (action.equals("ByAntiPerimeter")) { _shapes.sort(ShapeComp.CompByAntiPerimter); }
		if (action.equals("ByToString")) { _shapes.sort(ShapeComp.CompByToString); }
		if (action.equals("ByAntiToString")) { _shapes.sort(ShapeComp.CompByAntiToString); }
		if (action.equals("ByTag")) { _shapes.sort(ShapeComp.CompByTag); }
		if (action.equals("ByAntiTag")) { _shapes.sort(ShapeComp.CompByAntiTag); }

		// redraw the shapes after the updates
		drawShapes();
	}
	
	public void mouseClicked(Point2D p) {
		System.out.println("Mode: " + _mode + ". Click location: " + p);  // debug info
		// Select menu
		if (_mode.equals("Point")) {   // under mode Point in select menu
			selectUnderPoint(p);       // call the function to select under the clicked point
		}

		// Shape menu
		if (_mode.equals("Circle") || _mode.equals("Segment") || _mode.equals("Rect")) {    // these are modes with two clicks
			if (_lastClick == null) {            // if it is the first click
				_lastClick = new Point2D(p);     // save it
				return;                          // and return
			} else {                // if it is the second click
				finalizeShape();    // finalize the preview shape
			}
		}
		if (_mode.equals("Triangle")) {   // in this mode we accpet three click
			if (_lastClick == null) {           // if we don't have a last click this is the first point of the triangle
				_polyPoints = new ArrayList<Point2D>();  // initialize the point list
				_lastClick = new Point2D(p);    // remember this click
				_polyPoints.add(p);             // and add it to the list
				return;
			} else if (_polyPoints.size() == 1) { // if we have a last click but only one point in the list (second click)
				_polyPoints.add(p);               // we add the current point as well
				return;
			} else {                              // otherwise we have enough points
				finalizeShape();                  // so we finalize the shape
			}
		}
		if (_mode.equals("Polygon")) {
			if (_lastClick == null) {                    // if this is the first click of the polygon
				_polyPoints = new ArrayList<Point2D>();  // initialize the point list
				_previewShape.setShape(new Polygon2D(_polyPoints.toArray(new Point2D[0]))); // set the preview shape as an empty polygon
			}
			_polyPoints.add(p);               // add the clicked point to the point list
			_lastClick = new Point2D(p);
		}

		// Edit menu
		if (_mode.equals("Move")) {              // this is also a two click operation
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D moveVec = new Point2D(p.x() - _lastClick.x(), p.y() - _lastClick.y());   // create a vector between the clicks
				moveSelected(moveVec);       // move the selected by the vector
				_lastClick = null;           // nullify to indicate next click is the first again
			}
		}
		if (_mode.equals("Copy")) {          // same thing as last option
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D moveVec = new Point2D(p.x() - _lastClick.x(), p.y() - _lastClick.y());
				copySelected(moveVec);
				_lastClick = null;
			}
		}
		if (_mode.equals("Rotate")) {   // same thing again
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D rotateVector = _lastClick.vector(p);                  // except here we need to calculate the rotation angle by the vector
				double rotateAngleDegrees = rotateVector.angleDegrees();
				rotateSelected(_lastClick, rotateAngleDegrees);       // and rotate around the first click
				_lastClick = null;
			}
		}
		if (_mode.equals("Scale_90%")) {   // the next two options are one click operations with specific values
			scaleSelected(p, 0.9);
		}
		if (_mode.equals("Scale_110%")) {
			scaleSelected(p, 1.1);
		}

		drawShapes();   // redraw the shapes after the update
	}
	public void mouseRightClicked(Point2D p) {       // this is for finalizing a polygon, or cancelling a shape
		if (_polyPoints == null) {                   // if we are not currently drawing a polygon
			cancelShape();                           // cancel the current shape
			drawShapes();                            // redraw shapes
			if (DEBUG) {            // for debugging, draw the bounding box
				Rect2D boundingBox = _shapes.getBoundingBox();
				GUI_Shapeable gBoundingBox = new GUIShape(boundingBox, false, Color.RED, -1);
				_shapes.add(gBoundingBox);
			}
			return;  // return, the rest is for polygon
		}
		int numPoints = _polyPoints.size();     // get the number of points in the polygon
		if (numPoints < 2) {         // less than 2 is too few to create a shape
			cancelShape();           // so cancel it
			return;
		} else if (numPoints < 3) {  // less than 3 means we have a segment
			_previewShape.setShape(new Segment2D(_polyPoints.get(0), _polyPoints.get(1)));   // so we set the preview shape to a segment with the points
		} else if (numPoints < 4) {  // less than 4 means we have a triangle
			_previewShape.setShape(new Triangle2D(_polyPoints.get(0), _polyPoints.get(1), _polyPoints.get(2)));  // set the preview shape to a triangle
		} else {
			_previewShape.setShape(new Polygon2D(_polyPoints.toArray(new Point2D[0])));   // set the preview shape as a new polygon with the point list, this removes the current mouse position since it is not needed
		}
		_polyPoints = null;          // nullify the list after it has been used
		finalizeShape();             // finalize the shape
		drawShapes();                // update the screen
	}
	
	private void selectUnderPoint(Point2D p) {
		for(int i = 0; i < _shapes.size(); ++i) {   // iterate on the shapes
			GUI_Shapeable s = _shapes.get(i);                // get each GUIShape
			GeoShapeable g = _shapes.get(i).getShape();      // and its GeoShapeable
			if (g != null && g.contains(p)) {     // if the GeoShapeable contains the point
				s.setSelected(!s.isSelected());   // reverse the GUIShape's selection
			}
		}
	}
	private void moveSelected(Point2D moveVec) {
		for (int i = 0; i < _shapes.size(); ++i) {  // iterate on the shapes
			GUI_Shapeable s = _shapes.get(i);       // get both kinds
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {   // if it is selected
				g.move(moveVec);   // move by the provided vector
			}
		}
	}
	private void scaleSelected(Point2D center, double ratio) {   // same thing as last function with different paramteres
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {
				g.scale(center, ratio);
			}
		}
	}
	private void rotateSelected(Point2D center, double angleDegrees) {  // same thing as last function
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {
				g.rotate(center, angleDegrees);
			}
		}
	}
	private void removeSelected() {
		for (int i = _shapes.size() - 1; i >= 0; --i) {  // iterate on the shapes backwards to make sure indexes don't change for shapes we didn't check yet
			GUI_Shapeable s = _shapes.get(i);            // get the shape at the index
			if (s.isSelected()) {                        // if it is selected
				_shapes.removeElementAt(i);              // remove it from the canvas
			}
		}
	}
	private void copySelected(Point2D moveVec) {     // copy creates a copy and moves it by the vector
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {   // for each selected shape
				GUI_Shapeable copy = s.copy();   // create a copy
				copy.getShape().move(moveVec);   // move it
				copy.setTag(_runningTag++);      // set the tag to the current running tag and increment it
				_shapes.add(copy);               // add it to the canvas
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
	//	System.out.println("M: "+x+","+y);              // debug info, don't use unnecessarily as it creates a lot of trash in stdout

		double x = StdDraw_Ex4.mouseX();                // save mouse coordinates
		double y = StdDraw_Ex4.mouseY();
		Point2D p = new Point2D(x, y);                  // create a point for the current mouse position

		if (DEBUG) {                                    // debug mode operations
			for (int i = 0; i < _shapes.size(); ++i) {  // fill shapes with mouse on them
				GUI_Shapeable gs = _shapes.get(i);
				if (gs.getTag() != -1) gs.setFilled(gs.getShape().contains(p)); // tag -1 is for the bounding box, filling it would cover everything
			}
			drawShapes();
		}

		if (_lastClick == null) return;                 // if we are not currently drawing a shape nothing needs to be handled
		if (_mode.equals("Circle")) {
			double r = _lastClick.distance(p);          // calculate the radius for preivewed circle
			_previewShape.setShape(new Circle2D(_lastClick, r));      // set the preview shape as a circle centered on last click with the radius
		}
		if (_mode.equals("Segment")) {
			_previewShape.setShape(new Segment2D(_lastClick, p));     // set the preview as a segment from last click to mouse position
		}
		if (_mode.equals("Rect")) {
			_previewShape.setShape(new Rect2D(_lastClick, p));
		}
		if (_mode.equals("Polygon")) {
			assert _previewShape instanceof Polygon2D : "Preview shape is not polygon in mode Polygon";    
			Point2D[] previewPoints = new Point2D[_polyPoints.size() + 1];   // the points for the preview polygon are the save points with the current mouse position
			System.arraycopy(_polyPoints.toArray(), 0, previewPoints, 0, _polyPoints.size());   // copy the save points
			previewPoints[_polyPoints.size()] = p;                                  // add the current mouse position, note that inside previewPoly is a shallow copy of _polyPoints, so the point is not added to the list
			_previewShape.setShape(new Polygon2D(previewPoints));                   // set the preview shape as the preview poly
		}
		if (_mode.equals("Triangle")) {                               // if we are drawing a triangle
			if (_polyPoints.size() == 1) {                            // and have only one point so far
				_previewShape.setShape(new Segment2D(_lastClick, p)); // set the preview as a segment from last click to mouse position, indicating the first line of the triangle
			} else if (_polyPoints.size() > 1) {                      // otherwise (and if we have more than one point) we have enough points to preview a triangle (including the current mouse position)
				_previewShape.setShape(new Triangle2D(_polyPoints.get(0), _polyPoints.get(1), p));  
			}
		}

		drawShapes();                                   // update the screen
	}
	@Override
	public ShapeCollectionable getShape_Collection() {
		return this._shapes;
	}
	@Override
	public void show() { show(Ex4_Const.DIM_SIZE); }
	@Override
	public String getInfo() {
		return _shapes.toString();
	}
	/**
	 * This function adds the preview shape to the canvas's collection
	 */
	private void finalizeShape() {
		assert _previewShape != null : "Trying to finalize null shape";  // make sure we have a shape
		GUI_Shapeable newShape = _previewShape.copy();     // create a copy of it (_previewShape is a resereved variable and not to be tampered with)
		newShape.setColor(_color);          // set its parameters based on current values
		newShape.setFilled(_fill);
		newShape.setTag(_runningTag++);     // set the tag and increment the running tag
		_shapes.add(newShape);              // add the new-born shape to the collection
		_lastClick = null;                  // nullify last click, whatever just happened, the next click is a new shape
		_previewShape.setShape(null);       // set the preview GeoShapeable to null to signal that no shape is to be previewed now
	}
	/**
	 * This function cancels the preview shape
	 */
	private void cancelShape() {
		assert _previewShape != null : "Trying to cancel null shape";
		_lastClick = null;               // we just need to nullify last click and the preview GeoShapeable
		_previewShape.setShape(null);
	}
	/**
	 * This function shows the file selection dialog and returns the selected file
	 * @param mode a FileDialogMode, either SAVE or LOAD
	 * @return the absolute path to the chosen file
	 */
	private String chooseFile(FileDialogMode mode) {
		FileDialog chooser;   // create a FileDialog variable
		switch (mode) {
			case LOAD:
				chooser = new FileDialog(new Frame(), "Choose file to load", FileDialog.LOAD);  // initialize the FileDialog with appropriate parameters
				break;
			case SAVE:
				chooser = new FileDialog(new Frame(), "Choose where to save", FileDialog.SAVE);
				break;
			default:
				throw new IllegalArgumentException("UNREACHABLE: Illegal FileDialogMode");
		}
		chooser.setVisible(true);    // show the FileDialog
		return chooser.getDirectory() + chooser.getFile();   // return the absolute path
	}
}
