package intro2cs_exercises;

import java.util.Comparator;

public class PersonComparator implements Comparator<Person> {    
    public enum PersonCompareType {
        BY_ID,
        BY_HEIGHT,
        BY_NUMBER_OF_CHILDREN,
    }

    private PersonCompareType compareType = PersonCompareType.BY_ID;

    @Override
    public int compare(Person o1, Person o2) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
