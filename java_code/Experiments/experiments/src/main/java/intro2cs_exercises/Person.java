package intro2cs_exercises;

import java.util.ArrayList;

public class Person {
    private int _ID;
    private double _height;
    private ArrayList<Person> _children;

    private static int runningID = 0;

    Person(double height) {
        _ID = runningID++;
        _height = height;
    }

    public int getID() { return _ID; }

    public double getHeight() { return _height; }

    public final ArrayList<Person> getChildren() { return _children; }

    public boolean addChild(Person child) {
        return _children.add(child);
    }
}
