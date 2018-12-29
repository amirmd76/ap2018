public class Animal {

    private Pair<Long, Long> location, direction;        // Location on the map [x,y]
    private int speed;            //Speed of walking depends on type of animal
    private int level;
    private String type;
    private boolean alive;       // To check if yhe animal is dead or not
    private int id;             // ID is for finding this specific animal
    private int SUF;            // Speed Upgrade Factor
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

    public String walk(long time){          // direction is  a vector [dx,dy]
        location.x += direction.x * speed * time;        // Applying changes to x
        location.y += direction.y * speed * time;        // Applying changes to y
        return String.format("%s%d is now in the location of %d,%d", type, id, location.x, location.y);
    }

    public String die(){
        alive = false;
        return String.format("%s%d is dead!", type, id);
    }

    public String upgrade(){                    //TODO Check if this method should be abstract or not or just be in cat and dog class
        ++level;
        speed += SUF;
        return String.format("%s%d has upgraded to level %d", type, id, level);
    }

}
