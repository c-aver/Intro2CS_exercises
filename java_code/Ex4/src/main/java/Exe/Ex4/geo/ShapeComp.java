/**
 * Name: Chaim Averbach
 * ID: 207486473
 */

package Exe.Ex4.geo;

import java.util.Comparator;

import Exe.Ex4.Ex4_Const;
import Exe.Ex4.GUI_Shapeable;

/**
 * This class represents a Comparator over GUI_Shapes - 
 * as a linear order over GUI_Shapes.
 * Ex4: you should implement this class!
 * @author c-aver
 *
 */
public class ShapeComp implements Comparator<GUI_Shapeable>{
	
	public static final Comparator<GUI_Shapeable> CompByTag = new ShapeComp(Ex4_Const.Sort_By_Tag);
	public static final Comparator<GUI_Shapeable> CompByAntiTag = new ShapeComp(Ex4_Const.Sort_By_Anti_Tag);
	public static final Comparator<GUI_Shapeable> CompByArea = new ShapeComp(Ex4_Const.Sort_By_Area);
	public static final Comparator<GUI_Shapeable> CompByAntiArea = new ShapeComp(Ex4_Const.Sort_By_Anti_Area);
	public static final Comparator<GUI_Shapeable> CompByPerimter = new ShapeComp(Ex4_Const.Sort_By_Perimeter);
	public static final Comparator<GUI_Shapeable> CompByAntiPerimter = new ShapeComp(Ex4_Const.Sort_By_Anti_Perimeter);
	public static final Comparator<GUI_Shapeable> CompByToString = new ShapeComp(Ex4_Const.Sort_By_toString);
	public static final Comparator<GUI_Shapeable> CompByAntiToString = new ShapeComp(Ex4_Const.Sort_By_Anti_toString);
	
	private int _flag;
	public ShapeComp(int flag) {
		_flag = flag;
	}

	// For comparing we simply take the appropriate field and use its built-in comparator
	@Override
	public int compare(GUI_Shapeable o1, GUI_Shapeable o2) {
		if (_flag == Ex4_Const.Sort_By_Tag) {
			return Integer.compare(o2.getTag(), o1.getTag());
		}
		if (_flag == Ex4_Const.Sort_By_Anti_Tag) {
			return Integer.compare(o1.getTag(), o2.getTag());
		}
		if (_flag == Ex4_Const.Sort_By_Area) {
			return Double.compare(o1.getShape().area(), o2.getShape().area());
		}
		if (_flag == Ex4_Const.Sort_By_Anti_Area) {
			return Double.compare(o2.getShape().area(), o1.getShape().area());
		}
		if (_flag == Ex4_Const.Sort_By_Perimeter) {
			return Double.compare(o1.getShape().perimeter(), o2.getShape().perimeter());
		}
		if (_flag == Ex4_Const.Sort_By_Anti_Perimeter) {
			return Double.compare(o2.getShape().perimeter(), o1.getShape().perimeter());
		}
		if (_flag == Ex4_Const.Sort_By_toString) {
			return o1.toString().compareTo(o2.toString());
		}
		if (_flag == Ex4_Const.Sort_By_Anti_toString) {
			return o2.toString().compareTo(o1.toString());
		}
		throw new IllegalArgumentException("Illegal Shape Comparator flag");
	}
}
