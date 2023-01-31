package zoo;

public abstract class Animal {
   static int num_of_animals=0;
   protected String _name;
   protected double _weight;
public Animal(String name, double x)
{
_name= name;
_weight=x;
num_of_animals++;
}
public abstract void roar();
}
