package intro2cs_exercises;

import java.util.ArrayList;

import intro2cs_exercises.PersonComparator.PersonCompareType;

public class Person implements Comparable<Person> {
    private int _ID;
    private double _heightInMeters;
    private ArrayList<Person> _children;

    private static int runningID = 0;

    Person(double heightInMeters, Person[] parents) {
        _ID = runningID++;
        _heightInMeters = heightInMeters;
        _children = new ArrayList<Person>();
        if (parents != null)
            for (Person parent : parents) {
                parent.addChild(this);
            }
    }

    public int getID() { return _ID; }

    public double getHeight() { return _heightInMeters; }

    public final ArrayList<Person> getChildren() { return _children; }

    public boolean addChild(Person child) {
        return _children.add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Person)) return false;
        Person otherPerson = (Person) o;
        return otherPerson.getID() == _ID;
    }

    @Override
    public int compareTo(Person otherP) {
        return new PersonComparator().compare(this, otherP);
    }
    public int compareTo(Person otherP, PersonCompareType compareType) {
        return new PersonComparator(compareType).compare(this, otherP);
    }
}
