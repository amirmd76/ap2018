import org.json.JSONException;
import org.json.JSONObject;

public class Wild extends Animal {

    private boolean prisoned = false;         //For stating that this wild animal is in the prisoned or not
    private int size = 1;
    private int prisonedTime = 0;

    public Wild(int ID, String type, long x, long y, int speed, int SUF) {
        super(ID, x, y, speed, type, SUF);
        prisoned = false;
        switch (this.getType()) {
            case "Lion": { this.size = 20; break;}
            case "Bear": { this.size = 20; break;}
            default: {this.size = 0;}
        }
    }

    @Override
    public JSONObject dump() {
        JSONObject object = super.dump();
        try {
            object.put("prisoned", prisoned);
            object.put("size", size);
            object.put("prisonedTime", prisonedTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Wild(JSONObject object) {
        super(object);
        try {
            prisoned = object.getBoolean("prisoned");
            prisonedTime = object.getInt("prisonedTime");
            size = object.getInt("size");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void breakOut() { prisoned = false; }
    public boolean isPrisoned() { return prisoned; }

    public int getPrisonedTime() {
        return prisonedTime;
    }

    public String kill(FarmAnimal farmAnimal){      // Killing farm animals
        farmAnimal.die();
        return String.format("%s%d has killed %s%d", this.getType(), this.getId(), farmAnimal.getType(), farmAnimal.getId());
    }

    public String destroy(Product product){         //Destroying products on the ground
        product.destroy();
        return String.format("%s%d had destroyed %s", this.getType(), this.getId(), product.getType());
    }

    public int getSize() {
        return size;
    }

    public String prison(int time){
        prisoned = true;
        prisonedTime = time;
        this.setSpeed(0);
        return String.format("%s%d has been prisoned", getType(), getId());
    }
}
