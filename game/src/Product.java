import org.json.JSONObject;
import sun.security.krb5.internal.crypto.Des;

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
            default: {this.size = 0;}
        }
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        if(location != null)
            object.put("location", location.dump());
        object.put("type", type);
        object.put("inStorage", inStorage);
        object.put("Destroyed", Destroyed);
        object.put("size", size);
        object.put("creationTime", creationTime);
        return object;
    }

    public Product(JSONObject object) {
        if(object.has("location"))
            location = new Pair<>(object.getJSONObject("location"));
        type = object.getString("type");
        inStorage = object.getBoolean("inStorage");
        Destroyed = object.getBoolean("Destroyed");
        size = object.getInt("size");
        creationTime = object.getInt("time");
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
