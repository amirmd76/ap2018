public class Animal {

    protected Pair<Long, Long> location, direction;        // Location on the map [x,y]
    protected int speed;            //Speed of walking depends on type of animal
    protected int level;
    protected String type;
    protected boolean alive;       // To check if yhe animal is dead or not
    protected int id;             // ID is for finding this specific animal
    protected int SUF;            // Speed Upgrade Factor
                                //TODO handle SUf for each animal in constants

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
        this.direction = direction;
    }

    public void setLocation(Pair<Long, Long> location) {
        this.location = location;
    }

    public String walk(long time){          // direction is  a vector [dx,dy]
        if(time == 0)
            return "";
        location.x += direction.x * speed * time;        // Applying changes to x
        location.y += direction.y * speed * time;        // Applying changes to y
        return String.format("%s%d is now in the location of %d,%d\n", type, id, location.x, location.y);
    }

    public String die(){
        alive = false;
        return String.format("%s%d is dead!", type, id);
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
