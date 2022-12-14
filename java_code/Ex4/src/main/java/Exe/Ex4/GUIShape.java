/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

 package Exe.Ex4;
/**
 * This class implements the GUI_shape.
 * Ex4: you should implement this class!
 * @author c-aver
 */
import java.awt.Color;

import Exe.Ex4.geo.*;

public class GUIShape implements GUI_Shapeable {
	private GeoShapeable _g = null;
	private boolean _fill;
	private Color _color;
	private int _tag;
	private boolean _isSelected = false;
	
	public GUIShape(GeoShapeable gs, boolean fill, Color color, int tag) {
		_g = null;
		if (gs != null) { _g = gs.copy(); }
		_fill= fill;
		_color = color;
		_tag = tag;
		_isSelected = false;
	}
	public GUIShape(GUIShape ot) {
		this(ot._g.copy(), ot._fill, ot._color, ot._tag);
	}

	public GUIShape(String str) {
		init(str.split(","));
	} 
	
	@Override
	public GeoShapeable getShape() {
		return _g;
	}

	@Override
	public boolean isFilled() {
		return _fill;
	}

	@Override
	public void setFilled(boolean filled) {
		_fill = filled;
	}

	@Override
	public Color getColor() {
		return _color;
	}

	@Override
	public void setColor(Color cl) {
		_color = cl;
	}

	@Override
	public int getTag() {
		return _tag;
	}

	@Override
	public void setTag(int tag) {
		_tag = tag;
		
	}

	@Override
	public GUI_Shapeable copy() {
		GUI_Shapeable cp = new GUIShape(this);
		return cp;
	}
	@Override
	public String toString() {
		return "GUIShape," + _color.getRGB() + ',' + _fill + ',' + _tag + ',' + _g.getClass().getSimpleName() + ',' + _g;
	}
	private void init(String[] args) {
		if (!args[0].equals("GUIShape")) throw new IllegalArgumentException("Trying to initialze GUIShape with non-GUIShape string");
		try {
			_color = new Color(Integer.parseInt(args[1]));
			_fill = Boolean.parseBoolean(args[2]);
			_tag = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			System.err.println("ERROR: Got wrongly fomatted string for GUIShape initialize: " + e.getMessage());
			throw e;
		}
		String type = args[4];
		String[] pointStrings = new String[args.length - 5];
		System.arraycopy(args, 5, pointStrings, 0, pointStrings.length);
		switch (type) {
			case "Circle2D": 
				try {
					_g = new Circle2D(new Point2D(Double.parseDouble(args[5]), Double.parseDouble(args[6])), Double.parseDouble(args[7]));
				} catch (IllegalArgumentException e) {
					System.err.println("ERROR: Got wrongly formatted string for Circle2D GUIShape initialize: " + e.getMessage());
				}
				return;
			case "Segment2D":
				_g = new Segment2D(pointStrings);
				return;
			case "Triangle2D":
				_g = new Triangle2D(pointStrings);
				return;
			case "Rect2D":
				_g = new Rect2D(pointStrings);
				return;
			case "Polygon2D":
				_g = new Polygon2D(pointStrings);
				return;
		}
	}
	@Override
	public boolean isSelected() {
		return this._isSelected;
	}
	@Override
	public void setSelected(boolean s) {
		this._isSelected = s;
	}
	@Override
	public void setShape(GeoShapeable g) {
		this._g = g;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof GUIShape)) return false;
		GUIShape osh = (GUIShape) o;
		return this.getShape().equals(osh.getShape())
			&& this.isFilled() == osh.isFilled()
			&& this.getColor().equals(osh.getColor())
			&& this.getTag() == osh.getTag()
			&& this.isSelected() == osh.isSelected();
	}
}
