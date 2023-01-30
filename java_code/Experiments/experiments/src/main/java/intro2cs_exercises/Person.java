package intro2cs_exercises;

import java.text.DecimalFormat;
import java.util.ArrayList;

import intro2cs_exercises.PersonComparator.PersonCompareType;

public class Person implements Comparable<Person> {
    private int _ID;
    private double _heightInMeters;
    private ArrayList<Person> _children;

    private static int runningID = 0;

    public static final double MIN_HEIGHT = 1.0;
    public static final double MAX_HEIGHT = 2.5;

    public static Person randPerson(ArrayList<Person> peopleSoFar) {
        Person[] parents;
        int numPeopleSoFar = peopleSoFar.size();
        if (numPeopleSoFar == 0) {
            parents = null;
        } else if (numPeopleSoFar == 1) {
            parents = new Person[] {peopleSoFar.get(0)};
        } else {
            int parent1Index = (int) (Math.random() * numPeopleSoFar);
            int parent2Index = (int) (Math.random() * numPeopleSoFar);
            if (parent2Index == parent1Index) parent2Index = (parent2Index + 1) % numPeopleSoFar;
            parents = new Person[] { peopleSoFar.get(parent1Index), peopleSoFar.get(parent2Index) };
        }
        double height = Math.random() * (MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;
        return new Person(height, parents);
    }

    Person(Person orig) {
        _ID = orig.getID();
        _heightInMeters = orig.getHeight();
        _children = orig.getChildren();
    }

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

    public boolean setHeight(double heightInMeters) {
        boolean valid = heightInMeters >= MIN_HEIGHT && heightInMeters <= MAX_HEIGHT;
        if (valid) _heightInMeters = heightInMeters;
        return valid;
    }

    public final ArrayList<Person> getChildren() { return _children; }

    public boolean addChild(Person child) {
        return _children.add(child);
    }

    @Override
    public Person clone() {
        return new Person(this);
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Person)) return false;
        Person otherPerson = (Person) o;
        return otherPerson.getID() == _ID;
    }
    @Override
    public int hashCode() {
        return getID();
    }
    @Override
    public String toString() {
        String result = "";
        result += "ID: " + getID() + ". Height: " + new DecimalFormat("0.00").format(getHeight()) + "m.";
        if (_children.size() > 0) {
            result += " Children IDs: " + _children.get(0).getID();
            for (Person child : getChildren().subList(1, getChildren().size())) {
                result += ", " + child.getID();
            }
            result += ".";
        }
        return result;
    }

    @Override
    public int compareTo(Person otherP) {
        return new PersonComparator().compare(this, otherP);
    }
    public int compareTo(Person otherP, PersonCompareType compareType) {
        return new PersonComparator(compareType).compare(this, otherP);
    }
}
