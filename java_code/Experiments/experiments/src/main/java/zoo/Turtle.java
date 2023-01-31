package zoo;

public class Turtle extends Animal{
double carapace_size;
public Turtle(String name, double weight, double size)
{
    super (name,weight);
    carapace_size= size;
}
public void roar()
{
    System.out.println("Squik");
}
public Turtle child(Animal a)
{
    String s= "Son_of_"+this._name+"_and_"+a._name;

   return new Turtle(s, 3, this.carapace_size) ;
}
}
