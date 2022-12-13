package intro2cs_exercises;

import java.util.Comparator;

public class PersonComparator implements Comparator<Person> {    
    public enum PersonCompareType {
        BY_ID,
        BY_HEIGHT,
        BY_NUMBER_OF_CHILDREN,
    }

    private PersonCompareType _compareType = PersonCompareType.BY_ID;

    PersonComparator() {
        _compareType = PersonCompareType.BY_ID;
    }

    PersonComparator(PersonCompareType compareType) {
        _compareType = compareType;
    }

    @Override
    public int compare(Person p1, Person p2) {
        switch (_compareType) {
        case BY_ID:
            return p2.getID() - p1.getID();
        case BY_HEIGHT:
            return Double.compare(p1.getHeight(), p2.getHeight());
        case BY_NUMBER_OF_CHILDREN:
            return p1.getChildren().size() - p2.getChildren().size();
        default:
            throw new AssertionError("Unreachable");
        }
    }
}
