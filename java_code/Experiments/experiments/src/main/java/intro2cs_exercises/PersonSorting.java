package intro2cs_exercises;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PersonSorting {
    public static void main(String[] args) {
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 0; i < 10; ++i) {
            people.add(Person.randPerson(people));
        }
        for (Person person : people) {
            System.out.println(person);
        }
        Comparator<Person> comp = new Comparator<Person>() {
            public int compare(Person p1, Person p2) {
                return Double.compare(p1.getHeight(), p2.getHeight());
            }
        };
        Collections.sort(people, comp);
        System.out.println("\nAfter sorting:");
        for (Person person : people) {
            System.out.println(person);
        }
        Collections.sort(people, (p1, p2) -> Double.compare(p2.getHeight(), p1.getHeight()));
        System.out.println("\nAfter reverse sorting:");
        for (Person person : people) {
            System.out.println(person);
        }
    }

    public static int differentClasses(ArrayList<?> al) {
        Set<Class<?>> found = new HashSet<Class<?>>();
        for (Object o : al) {
            found.add(o.getClass());
        }
        return found.size();
    }
}
