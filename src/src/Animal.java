public class Animal {
    protected Pair<Long, Long> location, direction;
    protected long level;
    protected String type;
    protected boolean alive = true;
    public Animal(String type, long x, long y) {
        this.type = type;
        location = new Pair<>(x, y);
        direction = new Pair<>(0L, 0L);
        level = 1;
    }

    public void walk(String dir) {
        switch (dir) {
            case "left":
                setDirection(new Pair<>(0L, -1L));
                break;
            case "right":
                setDirection(new Pair<>(0L, 1L));
                break;
            case "up":
                setDirection(new Pair<>(-1L, 0L));
                break;
            case "down":
                setDirection(new Pair<>(1L, 0L));
                break;
        }
    }

    public void move(long time) {
        location.x += direction.x * time;
        location.y += direction.y * time;
    }

    public void setDirection(Pair<Long, Long> direction) {
        this.direction = direction;
    }
    public String getType() {
        return type;
    }
    public Pair<Long, Long> getLocation() {
        return location;
    }
    public boolean isAlive() {
        return alive;
    }
}
