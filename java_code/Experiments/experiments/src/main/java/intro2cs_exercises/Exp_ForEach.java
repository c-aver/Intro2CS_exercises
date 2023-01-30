package intro2cs_exercises;

import java.util.ArrayList;

public class Exp_ForEach {
    public static void main(String[] args) {
        ArrayList<Person> people = new ArrayList<Person>();
        for (int i = 0; i < 10; ++i) people.add(Person.randPerson(people));
        people.forEach(p -> {
            if (p.getChildren().size() > 0) System.out.println(p);
            else {
                p.setHeight(1.8);
            }
        });
        System.out.println();
        people.forEach(p -> System.out.println(p));
    }
}
