import org.json.JSONObject;

public class Well implements UpgradeableObject {
    private long capacity, storedWater;
    private int level;
    public int stage = 0;
    public boolean working = false;

    public Well() {
        this.capacity = Constants.Well_Water_Capacity[0];
        this.storedWater = capacity;
        this.level = 1;
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("capacity", capacity);
        object.put("storedWater", storedWater);
        object.put("level", level);
        return object;
    }

    public Well(JSONObject object) {
        capacity = object.getLong("capacity");
        storedWater = object.getLong("storedWater");
        level = object.getInt("level");
    }

    public String print() {
        return String.format("Well(level %d) has capacity %d, and %d units water stored", level, capacity, storedWater);
    }

    public long getStoredWater() { return storedWater; }

    public int reFillCost() {
        return Constants.WELL_REFILL_COST[level-1];
    }

    public String reFill(){
        working = true;
        stage = 0;
        storedWater = capacity;
        return String.format("Well is full now");
    }

    public String useWater(){
        try {
            if (storedWater == 0) {
                Exception e = new Exception("Well is empty");           //TODO handling not enough water exception!!
                throw e;
            }
            else {
                storedWater--;
                return "Water is now provided for planting";
            }
        }
        catch (Exception e){
            return (e.toString());
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    public String upgrade() {
        try {                                               //Handling upgrading exception
            if (level == Constants.Well_Max_Level_Upgrade){
                throw new Exception("Well is fully upgraded");
            }
            else {
                level ++;
                capacity = Constants.Well_Water_Capacity[level-1];
                storedWater = capacity;
                return String.format("Well is now upgraded to level %d", level);
            }
        }
        catch (Exception e){
            return e.toString();
        }
    }
}
