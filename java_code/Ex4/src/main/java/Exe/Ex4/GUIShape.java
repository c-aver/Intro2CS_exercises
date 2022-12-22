package Exe.Ex4;
/**
 * This class implements the GUI_shape.
 * Ex4: you should implement this class!
 * @author I2CS
 */
import java.awt.Color;

import Exe.Ex4.geo.GeoShapeable;


public class GUIShape implements GUI_Shapeable{
	private GeoShapeable _g = null;
	private boolean _fill;
	private Color _color;
	private int _tag;
	private boolean _isSelected;
	
	public GUIShape(GeoShapeable gs, boolean fill, Color color, int tag) {
		_g = null;
		if (gs != null) { _g = gs.copy(); }
		_fill= fill;
		_color = color;
		_tag = tag;
		_isSelected = false;
	}
	public GUIShape(GUIShape ot) {
		this(ot._g, ot._fill, ot._color, ot._tag);
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
	/**
	 * This functions returns the name a Color object.
	 * Only for the color options presented in the Ex4 GUI.
	 * @param col the color to be named
	 * @return a string containing the name of the color
	 */
	private String colorName(Color col) {
		if (col.equals(Color.WHITE))  return "white";
		if (col.equals(Color.BLACK))  return "black";
		if (col.equals(Color.BLUE))   return "blue";
		if (col.equals(Color.RED))    return "red";
		if (col.equals(Color.YELLOW)) return "yellow";
		if (col.equals(Color.GREEN))  return "green";
		return "unknown color";
	}
	@Override
	public String toString() {
		return (_isSelected ? "*" : " ") + Integer.toString(_tag) + ": " + (_fill ? "Filled " : "Hollow ") + colorName(_color) + " " + _g.toString();
	}
	private void init(String[] ww) {
		// TODO: what is this?
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
}
