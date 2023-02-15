package intro2cs_exercises;

import intro2cs_exercises.PersonComparator.PersonCompareType;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class PersonTest 
{
    @Test
    public void testPersonComapre() {
        Person p1 = new Person(1.7, null);
        Person p2 = new Person(1.6, new Person[] {p1});
        assert p1.compareTo(p2) > 0;
        assert p1.compareTo(p2, PersonCompareType.BY_HEIGHT) > 0;
        assert p1.compareTo(p2, PersonCompareType.BY_NUMBER_OF_CHILDREN) > 0;
        assertEquals(p2, p1.getChildren().get(0));
    }
    @Test
    public void testRandPerson() {
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 0; i < 10; ++i) {
            people.add(Person.randPerson(people));
        }
        for (Person person : people) {
            System.out.println(person);
        }
    }
}
