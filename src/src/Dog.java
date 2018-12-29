import org.json.JSONObject;

public class Dog extends Animal implements UpgradeableObject {

    public Dog(int ID, long x, long y, int speed, int SUF) {
        super(ID, x, y, speed, "Dog", SUF);
    }

    public Dog(JSONObject object) {
        super(object);
    }

    public String kill(Wild wild){
        wild.die();
        this.die();
        return String.format("Both %s%d and %s%d are dead now", this.getType(), this.getId(), wild.getType(), wild.getId());
    }

    public String upgrade(){
        if(level >= 3)
            return "Dog is fully upgraded";
        speed += SUF;
        return String.format("%s%d has upgraded to level %d", type, id, level);
    }

}
