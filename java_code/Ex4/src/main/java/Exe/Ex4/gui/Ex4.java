package Exe.Ex4.gui;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFileChooser;

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
 * @author boaz.benmoshe
 *
 */
public class Ex4 implements Ex4_GUI{
	private final boolean DEBUG = false;  // debug mode
	static private final int LOAD = 0;
	static private final int SAVE = 1;

	private ShapeCollectionable _shapes = new ShapeCollection();  // the shapes in the canvas
	private GUI_Shapeable _previewShape = new GUIShape(null, false, Color.pink, 0);           // the shape currently being drawn, the only field that should be changed is its GeoShapeable
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
		if (s != null) _runningTag = s.size() + 1;
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
	
	public void drawShapes() {
		StdDraw_Ex4.clear();
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable sh = _shapes.get(i);
			drawShape(sh);
		}
		if (_previewShape.getShape() != null) { drawShape(_previewShape); }
		StdDraw_Ex4.show();
	}
	private static void drawShape(GUI_Shapeable g) {
		StdDraw_Ex4.setPenColor(g.getColor());
		if (g.isSelected()) { StdDraw_Ex4.setPenColor(Color.gray); }
		GeoShapeable gs = g.getShape();
		boolean isFill = g.isFilled();
		if (gs instanceof Circle2D) {
			Circle2D circle = (Circle2D) gs;
			Point2D center = circle.getPoints()[0];
			double rad = circle.getRadius();
			if (isFill) {
				StdDraw_Ex4.filledCircle(center.x(), center.y(), rad);
			} else { 
				StdDraw_Ex4.circle(center.x(), center.y(), rad);
			}
		}
		if (gs instanceof Segment2D) {
			Point2D[] ps = gs.getPoints();
			StdDraw_Ex4.line(ps[0].x(), ps[0].y(), ps[1].x(), ps[1].y());
		}
		if (gs instanceof Polygon2D || gs instanceof Triangle2D || gs instanceof Rect2D) {
			Point2D[] ps = gs.getPoints();
			double[] xs = new double[ps.length], ys = new double[ps.length];
			for (int i = 0; i < ps.length; ++i) {
				xs[i] = ps[i].x();
				ys[i] = ps[i].y();
			}
			if (g.isFilled()) {
				StdDraw_Ex4.filledPolygon(xs, ys);
			} else {
				StdDraw_Ex4.polygon(xs, ys);
			}
		}
	}
	private void updateColor(Color c) {
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
		if (action.equals("Save")) {
			String filePath = chooseFile(SAVE);
			if (filePath != null) _shapes.save(filePath);
		}
		if (action.equals("Load")) {
			String filePath = chooseFile(LOAD);
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
				if (shape.isSelected()) System.out.println(shape); // if the shape is selected print its info
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
		if (action.equals("ByArea")) { _shapes.sort(ShapeComp.CompByArea); }
		if (action.equals("ByAntiArea")) { _shapes.sort(ShapeComp.CompByAntiArea); }
		if (action.equals("ByPerimeter")) { _shapes.sort(ShapeComp.CompByPerimter); }
		if (action.equals("ByAntiPerimeter")) { _shapes.sort(ShapeComp.CompByAntiPerimter); }
		if (action.equals("ByToString")) { _shapes.sort(ShapeComp.CompByToString); }
		if (action.equals("ByAntiToString")) { _shapes.sort(ShapeComp.CompByAntiToString); }
		if (action.equals("ByTag")) { _shapes.sort(ShapeComp.CompByTag); }
		if (action.equals("ByAntiTag")) { _shapes.sort(ShapeComp.CompByAntiTag); }

		drawShapes();
	}
	
	public void mouseClicked(Point2D p) {
		System.out.println("Mode: " + _mode + ". Click location: " + p);
		// Select menu
		if (_mode.equals("Point")) {
			selectUnderPoint(p);
		}

		// Shape menu
		if (_mode.equals("Circle") || _mode.equals("Segment") || _mode.equals("Rect")) {
			if(_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				finalizeShape();
			}
		}
		if (_mode.equals("Triangle")) {
			if (_lastClick == null) {           // if we don't have a last click this is the first point of the triangle
				_polyPoints = new ArrayList<Point2D>();  // initialize the point list
				_lastClick = new Point2D(p);    // so we remember it
				_polyPoints.add(p);
				return;
			} else if (_polyPoints.size() == 1) { // if we have a last click but only one point in the list
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
		if (_mode.equals("Move")) {
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D moveVec = new Point2D(p.x() - _lastClick.x(), p.y() - _lastClick.y());
				moveSelected(moveVec);
				_lastClick = null;
			}
		}
		if (_mode.equals("Copy")) {
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D moveVec = new Point2D(p.x() - _lastClick.x(), p.y() - _lastClick.y());
				copySelected(moveVec);
				_lastClick = null;
			}
		}
		if (_mode.equals("Remove")) {
			removeSelected();
		}
		if (_mode.equals("Rotate")) {
			if (_lastClick == null) {
				_lastClick = new Point2D(p);
				return;
			} else {
				Point2D rotateVector = _lastClick.vector(p);
				double rotateAngleDegrees = rotateVector.angleDegrees();
				rotateSelected(_lastClick, rotateAngleDegrees);
				_lastClick = null;
			}
		}
		if (_mode.equals("Scale_90%")) {
			scaleSelected(p, 0.9);
		}
		if (_mode.equals("Scale_110%")) {
			scaleSelected(p, 1.1);
		}

		drawShapes();
	}
	public void mouseRightClicked(Point2D p) {
		if (_polyPoints == null) {
			cancelShape();
			if (DEBUG) {
				Rect2D boundingBox = _shapes.getBoundingBox();
				GUI_Shapeable gBoundingBox = new GUIShape(boundingBox, false, Color.RED, -1);
				_shapes.add(gBoundingBox);
			}
			return;
		}
		int numPoints = _polyPoints.size();
		if (numPoints < 2) {
			cancelShape();
			return;
		} else if (numPoints < 3) {
			_previewShape.setShape(new Segment2D(_polyPoints.get(0), _polyPoints.get(1)));
		} else if (numPoints < 3) {
			_previewShape.setShape(new Triangle2D(_polyPoints.get(0), _polyPoints.get(1), _polyPoints.get(2)));
		} else {
			_previewShape.setShape(new Polygon2D(_polyPoints.toArray(new Point2D[0])));   // set the preview shape as a new polygon with the point list, this removes the current mouse position since it is not needed
		}
		_polyPoints = null;          // nullify the list after it has been used
		finalizeShape();             // finalize the shape
		drawShapes();                // update the screen
	}
	
	private void selectUnderPoint(Point2D p) {
		for(int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (g != null && g.contains(p)) {
				s.setSelected(!s.isSelected());
			}
		}
	}
	private void moveSelected(Point2D moveVec) {
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {
				g.move(moveVec);
			}
		}
	}
	private void scaleSelected(Point2D center, double ratio) {
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {
				g.scale(center, ratio);
			}
		}
	}
	private void rotateSelected(Point2D center, double angleDegrees) {
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
	private void copySelected(Point2D moveVec) {
		for (int i = 0; i < _shapes.size(); ++i) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if (s.isSelected() && g != null) {
				GUI_Shapeable copy = s.copy();
				copy.getShape().move(moveVec);
				_shapes.add(copy);
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
				if (gs.getTag() != -1) gs.setFilled(gs.getShape().contains(p));
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
			Point2D[] previewPoints = new Point2D[_polyPoints.size() + 1];
			System.arraycopy(_polyPoints.toArray(), 0, previewPoints, 0, _polyPoints.size());
			previewPoints[_polyPoints.size()] = p;                                  // add the current mouse position, note that inside previewPoly is a shallow copy of _polyPoints, so the point is not added to the list
			_previewShape.setShape(new Polygon2D(previewPoints));                      // set the preview shape as the preview poly
		}
		if (_mode.equals("Triangle")) {                               // if we are drawing a triangle
			if (_polyPoints.size() == 1) {                            // and have only one point so far
				_previewShape.setShape(new Segment2D(_lastClick, p)); // set the preview as a segment from last click to mouse position, indicating the first line of the triangle
			} else if (_polyPoints.size() > 1) {                      // otherwise (and if we have more than one poitn) we have enough points to preview a triangle (including the current mouse position)
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
	 * This function adds a GUI shape to the canvas's collection
	 * @param g the Geo shape represented in the GUI shape
	 */
	private void finalizeShape() {
		assert _previewShape != null : "Trying to finalize null shape";
		GUI_Shapeable newShape = _previewShape.copy();
		newShape.setColor(_color);
		newShape.setFilled(_fill);
		newShape.setTag(_runningTag++);
		_shapes.add(newShape);
		_lastClick = null;
		_previewShape.setShape(null);
	}
	private void cancelShape() {
		assert _previewShape != null : "Trying to cancel null shape";
		_lastClick = null;
		_previewShape.setShape(null);
	}

	private String chooseFile(int mode) {
		JFileChooser chooser = new JFileChooser();
		int returnVal = (mode == SAVE ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null));
        if (returnVal == JFileChooser.APPROVE_OPTION) {   // if succesful
            return chooser.getSelectedFile().getAbsolutePath();   // return the chosen file
        }
		// otherwise, could be cancelled or errored
		return null;
	}
}
