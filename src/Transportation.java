import java.util.ArrayList;

public class Transportation {
    private String type;
    private long capacity;
    private int level,speed;

    public Transportation(String type) {
        this.type = type;
        switch (this.type) {
            case "truck" : {
                this.capacity = Constants.Truck_Initial_Capacity;
                this.speed = Constants.Truck_Initial_Speed;
                break;
            }
            case "helicopter" : {
                this.capacity = Constants.Helicopter_Initial_Capacity;
                this.speed = Constants.Helicopter_Initial_Speed;
                break;
            }
        }
        level = 1;
    }

    public String sell(String type, long count){                //TODO write sell code
        return null;
    }

    public String buy(String type, long count){                 //TODO write buy code
        return null;
    }

    public String upgrade() throws Exception{
        switch (type){
            case "truck": {
                try {
                    if (level == Constants.Truck_Max_Level_Upgrade)
                        throw new Exception("Truck is fully upgraded");
                    else {
                        level ++;
                        capacity += Constants.Truck_Upgrade_Capacity;
                        speed += Constants.Truck_Upgrade_Speed;
                        return String.format("Truck is now upraded to level %d", level);
                    }
                }
                catch (Exception e){
                    return e.toString();
                }
            }
            case "helicopter": {
                try {
                    if (level == Constants.Helicopter_Max_Level_Upgrade)
                        throw new Exception("Helicopter is fully upgraded");
                    else {
                        level ++;
                        capacity += Constants.Helicopter_Upgrade_Capacity;
                        speed += Constants.Helicopter_Upgrade_Speed;
                        return String.format("Helicopter is now upraded to level %d", level);
                    }
                }
                catch (Exception e){
                    return e.toString();
                }
            }
            default:
                return null;
        }
    }
}
