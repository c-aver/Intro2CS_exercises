package intro2cs_exercises;

import java.util.ArrayList;

import intro2cs_exercises.PersonComparator.PersonCompareType;

public class Person implements Comparable<Person> {
    private int _ID;
    private double _heightInMeters;
    private ArrayList<Person> _children;

    private static int runningID = 0;

    Person(double heightInMeters) {
        _ID = runningID++;
        _heightInMeters = heightInMeters;
        _children = new ArrayList<Person>();
    }

    public int getID() { return _ID; }

    public double getHeight() { return _heightInMeters; }

    public final ArrayList<Person> getChildren() { return _children; }

    public boolean addChild(Person child) {
        return _children.add(child);
    }

    @Override
    public int compareTo(Person otherP) {
        return new PersonComparator().compare(this, otherP);
    }
    public int compareTo(Person otherP, PersonCompareType compareType) {
        return new PersonComparator(compareType).compare(this, otherP);
    }
}
