import org.json.JSONObject;

public class Animal {

    protected Pair<Long, Long> location, direction;        // Location on the map [x,y]
    protected int speed;            //Speed of walking depends on type of animal
    protected int level;
    protected String type;
    protected boolean alive;       // To check if yhe animal is dead or not
    protected int id;             // ID is for finding this specific animal
    protected int SUF;            // Speed Upgrade Factor
                                //TODO handle SUf for each animal in constants
    public int stage = 0;

    public Animal(int ID, long x, long y, int speed, String type, int SUF) {
        location = new Pair<>(x, y);
        direction = new Pair<>(0L, 0L);
        this.speed = speed;
        this.type = type;
        this.level = 1;
        this.alive = true;
        this.id = ID;
        this.SUF = SUF;
    }

    public JSONObject dump() {
        JSONObject obj = new JSONObject();
        obj.put("location", location.dump());
        obj.put("direction", direction.dump());
        obj.put("speed", speed);
        obj.put("level", level);
        obj.put("type", type);
        obj.put("alive", alive);
        obj.put("id", id);
        obj.put("SUF", SUF);
        return obj;
    }

    public Animal(JSONObject object) {
        location = new Pair<>(object.getJSONObject("location"));
        direction = new Pair<>(object.getJSONObject("direction"));
        speed = object.getInt("speed");
        level = object.getInt("level");
        type = object.getString("type");
        alive = object.getBoolean("alive");
        id = object.getInt("id");
        SUF = object.getInt("SUF");
    }


    public String getType() { return type; }

    public boolean isAlive() {
        return alive;
    }

    public int getId() { return id; }

    public void setSpeed(int speed) { this.speed = speed; }

    public Pair<Long, Long> getLocation() {
        return location;
    }

    public int getLevel() { return level; }

    public void setDirection(Pair<Long, Long> direction) {
        if(this.direction == null || !this.direction.equals(direction))
            stage = 0;
        this.direction = direction;
    }

    public void setLocation(Pair<Long, Long> location) {
        this.location = location;
    }

    public String walk(Map map, long time){          // direction is  a vector [dx,dy]
        if(time == 0)
            return "";
        speed = 1;
        Pair<Long, Long> location = new Pair<>((long)this.location.x, (long)this.location.y);
        location.x += direction.x * speed * time;        // Applying changes to x
        location.y += direction.y * speed * time;        // Applying changes to y
        System.err.println(String.format("VALID CELL %d, %d", direction.x, direction.y));

        if(map.isValidCell(location)) {
            this.location = location;
        }
        else {
            setDirection(map.getRandomDirection());
        }
        return String.format("%s%d is now in the location of %d,%d\n", type, id, this.location.x, this.location.y);
    }

    public String die(){
        alive = false;
        return String.format("%s%d is dead!", type, id);
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
