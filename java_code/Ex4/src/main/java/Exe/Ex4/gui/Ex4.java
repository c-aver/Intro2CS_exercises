package Exe.Ex4.gui;
import java.awt.Color;
import java.awt.event.MouseEvent;

import Exe.Ex4.Ex4_Const;
import Exe.Ex4.GUIShape;
import Exe.Ex4.GUI_Shapeable;
import Exe.Ex4.ShapeCollection;
import Exe.Ex4.ShapeCollectionable;
import Exe.Ex4.geo.Circle2D;
import Exe.Ex4.geo.GeoShapeable;
import Exe.Ex4.geo.Point2D;
import Exe.Ex4.geo.Polygon2D;

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
	private ShapeCollectionable _shapes = new ShapeCollection();  // the shapes in the canvas
	private GUI_Shapeable _gs;
	private Color _color = Color.blue;   // the current brush color
	private boolean _fill = false;       // whether we are creating a filled shape
	private String _mode = "";           // the current brush mode
	private Point2D _lastClick;          // the last click location
	
	private static Ex4 _winEx4 = null;
	
	private Ex4() {
		init(null);
	}
	public void init(ShapeCollectionable s) {
		if (s == null) { _shapes = new ShapeCollection(); }
		else { _shapes = s.copy(); }
		GUI_Shapeable _gs = null; // TODO: do all these variables need to be inside the function or the fields?
		Polygon2D _pp = null; // TODO: what is this?
		_color = Color.blue;
		_fill = false;
		_mode = "";
		Point2D _p1 = null;
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
		for (int i = 0; i < _shapes.size(); ++i) { // TODO: foreach?
			GUI_Shapeable sh = _shapes.get(i);
			
			drawShape(sh);
		}
		if (_gs != null) { drawShape(_gs); }
		StdDraw_Ex4.show();
	}
	private static void drawShape(GUI_Shapeable g) {
		StdDraw_Ex4.setPenColor(g.getColor());
		if (g.isSelected()) { StdDraw_Ex4.setPenColor(Color.gray); }
		GeoShapeable gs = g.getShape();
		boolean isFill = g.isFilled();
		if (gs instanceof Circle2D) {
			Circle2D c = (Circle2D) gs;
			Point2D cen = c.getPoints()[0];
			double rad = c.getRadius();
			if (isFill) {
				StdDraw_Ex4.filledCircle(cen.x(), cen.y(), rad);
			}
			else { 
				StdDraw_Ex4.circle(cen.x(), cen.y(), rad);
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

	public void actionPerformed(String p) {
		_mode = p;
		if (p.equals("Blue"))   { _color = Color.BLUE;   updateColor(_color); }
		if (p.equals("Red"))    { _color = Color.RED;    updateColor(_color); }
		if (p.equals("Green"))  { _color = Color.GREEN;  updateColor(_color); }
		if (p.equals("White"))  { _color = Color.WHITE;  updateColor(_color); }
		if (p.equals("Black"))  { _color = Color.BLACK;  updateColor(_color); }
		if (p.equals("Yellow")) { _color = Color.YELLOW; updateColor(_color); }
		if (p.equals("Fill"))   { _fill = true; updateFill(); }
		if (p.equals("Empty"))  { _fill = false; updateFill(); }
		if (p.equals("Clear"))  { _shapes.removeAll(); }
	
		drawShapes();
		
	}
	
	public void mouseClicked(Point2D p) {
		System.out.println("Mode: " + _mode + ". Click location: " + p);
		if(_mode.equals("Circle")) {
			if(_gs == null) {
				_lastClick = new Point2D(p);
			}
			else {
				_gs.setColor(_color);
				_gs.setFilled(_fill);
				_shapes.add(_gs);
				_gs = null;
				_lastClick = null;
			}
		}
		if(_mode.equals("Move")) {
			if(_lastClick==null) {_lastClick = new Point2D(p);}
			else {
				_lastClick = new Point2D(p.x()-_lastClick.x(), p.y()-_lastClick.y());
				move();
				_lastClick = null;
			}
		}
		if(_mode.equals("Point")) {
			select(p);
		}
	
		drawShapes();
	}
	public void mouseRightClicked(Point2D p) {
		System.out.println("right click!");
	
	}
	
	private void select(Point2D p) {
		for(int i=0;i<_shapes.size();i++) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if(g!=null && g.contains(p)) {
				s.setSelected(!s.isSelected());
			}
		}
	}
	private void move() {
		for(int i=0;i<_shapes.size();i++) {
			GUI_Shapeable s = _shapes.get(i);
			GeoShapeable g = s.getShape();
			if(s.isSelected() && g!=null) {
				g.move(_lastClick);
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if(_lastClick!=null) {
			double x1 = StdDraw_Ex4.mouseX();
			double y1 = StdDraw_Ex4.mouseY();
			GeoShapeable gs = null;
		//	System.out.println("M: "+x1+","+y1);
			Point2D p = new Point2D(x1,y1);
			if(_mode.equals("Circle")) {
				double r = _lastClick.distance(p);
				gs = new Circle2D(_lastClick,r);
			}
	
			_gs = new GUIShape(gs,false, Color.pink, 0);
			drawShapes();
		}
	}
	@Override
	public ShapeCollectionable getShape_Collection() {
		// TODO Auto-generated method stub
		return this._shapes;
	}
	@Override
	public void show() {show(Ex4_Const.DIM_SIZE); }
	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		String ans = "";
		for(int i=0;i<_shapes.size();i++) {
			GUI_Shapeable s = _shapes.get(i);
			ans +=s.toString()+"\n";
		}
		return ans;
	}
}
