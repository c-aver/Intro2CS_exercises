package Exe.Ex4;

import java.util.ArrayList;
import java.util.Comparator;

import Exe.Ex4.geo.Rect2D;

/**
 * This class represents a collection of GUI_Shape.
 * Ex4: you should implement this class!
 * @author I2CS
 *
 */
public class ShapeCollection implements ShapeCollectionable{
	private ArrayList<GUI_Shapeable> _shapes;      // the collection of the shapes
	
	public ShapeCollection() {
		_shapes = new ArrayList<GUI_Shapeable>();  // initialize the ArrayList
	}
	@Override
	public GUI_Shapeable get(int i) {
		return _shapes.get(i);                     // call the ArrayList's function
	}

	@Override
	public int size() {
		return _shapes.size();                     // call the ArrayList's function
	}

	@Override
	public GUI_Shapeable removeElementAt(int i) {
		return _shapes.remove(i);                  // call the ArrayList's function
	}

	@Override
	public void addAt(GUI_Shapeable s, int i) {
		_shapes.add(i, s);                         // call the ArrayList's function
	}
	@Override
	public void add(GUI_Shapeable s) {
		if(s != null && s.getShape() != null) {
			_shapes.add(s);
		}
	}
	public void addAll(ShapeCollectionable c) {
		for (int i = 0; i < c.size(); ++i) {
			_shapes.add(c.get(i));
		}
	}
	@Override
	public ShapeCollectionable copy() {
		ShapeCollection result = new ShapeCollection();
		result.addAll(this);
		return result;
	}

	@Override
	public void sort(Comparator<GUI_Shapeable> comp) {
		_shapes.sort(comp);
	}

	@Override
	public void removeAll() {
		_shapes.clear();
	}

	@Override
	public void save(String file) {
		////////// TODO: add your code below ///////////
		
		
		//////////////////////////////////////////
	}

	@Override
	public void load(String file) {
		////////// TODO: add your code below ///////////
		
		
		//////////////////////////////////////////
	}
	@Override
	public Rect2D getBoundingBox() {
		Rect2D ans = null;
		////////// TODO: add your code below ///////////
		
		
		//////////////////////////////////////////
		return ans;
	}
	@Override
	public String toString() {
		String ans = "";
		for(int i = 0; i < size(); ++i) {
			ans += this.get(i).toString() + '\n';
		}
		return ans;
	}
}
