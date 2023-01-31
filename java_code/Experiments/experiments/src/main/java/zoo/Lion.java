package zoo;

public class Lion extends Animal {
    double _mane;
    public Lion(String name, double weight, double mane)
    {
        super(name, weight);
        _mane= mane;
    }
    public void roar()
    {
        System.out.println("Arrrrrrr");
    }

}
