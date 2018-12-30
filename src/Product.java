import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    Pair<Long, Long> location = null;         // Location on the map [x,y]
    String type;
    boolean inStorage, Destroyed;
    private int size = 0;
    private int creationTime = -1;

    public int getCreationTime() {
        return creationTime;
    }

    public Product(Pair<Long, Long> location, String type) {
        this.location = location;
        this.type = type;
        inStorage = false;
        Destroyed = false;
        switch (this.type) {                    //TODO handle other types of products that workshops can produce
            case "egg": { this.size = 1; break;}
            case "milk": { this.size = 5; break;}
            case "wool": { this.size = 10; break;}
            case "flour": {this.size = 2; break;}
            case "cake": {this.size = 5; break;}
            case "eggpowder": {this.size = 4; break;}
            case "cookie": {this.size = 4; break;}
            case "feather": {this.size = 15; break;}
            case "cotton": {this.size = 10; break;}
            case "string": {this.size = 6; break;}
            case "cloth": {this.size = 8; break;}
            default: {this.size = 0;}
        }
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        try {
            if(location != null)
                object.put("location", location.dump());
            object.put("type", type);
            object.put("inStorage", inStorage);
            object.put("Destroyed", Destroyed);
            object.put("size", size);
            object.put("creationTime", creationTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Product(JSONObject object) {
        try {
            if(object.has("location"))
                location = new Pair<>(object.getJSONObject("location"));
            type = object.getString("type");
            inStorage = object.getBoolean("inStorage");
            Destroyed = object.getBoolean("Destroyed");
            size = object.getInt("size");
            creationTime = object.getInt("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Product(Pair<Long, Long> location, String type, int time) {
        this(location, type);
        creationTime = time;
    }

    public String getType() { return type; }

    public Pair<Long, Long> getLocation() { return location; }

    public int getSize() { return size; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; location = new Pair<>(0L, 0L);}

    public void setLocation(Pair<Long, Long> location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
