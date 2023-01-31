package zoo;

public class Zoo {
    public static void main(String [] args)
    {
        Animal[] animals= new Animal[7];
        animals[0]= new Lion("Simba",3,14);
        animals[1]= new Goose("Uza",4,"egypt");
        animals[2] = new NinjaTurtle("Rafael",6,5,4);
        for (int i = 0; i < Animal.num_of_animals; i++) {
            animals[i].roar();
        }
        Turtle batz= new Turtle ("Batz",2,12);
        batz.roar();
        Turtle sonBatz1= batz.child(animals[0]);
        Turtle sonBatz2= batz.child(animals[1]);
    }
}
