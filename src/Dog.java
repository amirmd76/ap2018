public class Dog extends Animal {

    public Dog(int ID, String type, long x, long y, int speed, int SUF) {
        super(ID, x, y, speed, "Dog", SUF);
    }

    public String kill(Wild wild){
        wild.die();
        this.die();
        return String.format("Both %s%d and %s%d are dead now", this.getType(), this.getId(), wild.getType(), wild.getId());
    }


}
