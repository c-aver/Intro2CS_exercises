package intro2cs_exercises;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        double d = 5.0;
        int i = 3;
        int j = 5;
        String s = "Hello";
        Object o = new Object();
        ArrayList<Object> al = new ArrayList<Object>();
        al.add(d);
        System.out.println(d);
        al.add(i);
        System.out.println(i);
        al.add(j);
        System.out.println(j);
        al.add(s);
        System.out.println(s);
        al.add(o);
        System.out.println(o);
        System.out.println(differentClasses(al));
    }

    public static int differentClasses(ArrayList<?> al) {
        Set<Class<?>> found = new HashSet<Class<?>>();
        for (Object o : al) {
            found.add(o.getClass());
        }
        return found.size();
    }
}
