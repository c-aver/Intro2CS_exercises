package intro2cs_exercises;

import intro2cs_exercises.PersonComparator.PersonCompareType;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class PersonTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testPersonComapre() {
        Person p1 = new Person(1.7);
        Person p2 = new Person(1.6);
        p1.addChild(p2);
        assert p1.compareTo(p2) > 0;
        assert p1.compareTo(p2, PersonCompareType.BY_HEIGHT) > 0;
        assert p1.compareTo(p2, PersonCompareType.BY_NUMBER_OF_CHILDREN) > 0;
    }
}
