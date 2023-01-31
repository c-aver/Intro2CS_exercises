package intro2cs_exercises;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Exp_DiffClasses {
    public static void main(String[] args) {
        double d = 5.0;
        int i = 3;
        int j = 5;
        String s = "Hello";
        Object o = new Object();
        ArrayList<Object> al = new ArrayList<Object>();
        al.add(d);
        al.add(i);
        al.add(j);
        al.add(s);
        al.add(o);
        System.out.println(o);
        System.out.println(differentClasses(al));
    }

    public static int differentClasses(ArrayList<?> al) {
        Set<Class<?>> found = new HashSet<Class<?>>();
    //  for (Object o : al) {
    //      found.add(o.getClass());
    //  }
        found.addAll(al.stream().map(o -> o.getClass()).toList());
        found.forEach(c -> System.out.println(c.getSimpleName()));
        return found.size();
    }
}
