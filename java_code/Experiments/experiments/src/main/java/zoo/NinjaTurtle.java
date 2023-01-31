package zoo;

public class NinjaTurtle extends Turtle {
    int _weapon;

    public NinjaTurtle(String name, double weight, double size, int weapon) {
        super(name, weight, size);
        _weapon = weapon;
    }

    @Override
    public void roar() {
        System.out.println("kababanga");
    }
}
