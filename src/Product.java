public class Product {

    Pair<Long, Long> location;         // Location on the map [x,y]
    String type;
    boolean inStorage, Destroyed;

    public Product(Pair<Long, Long> location, String type) {
        this.location = location;
        this.type = type;
        inStorage = false;
        Destroyed = false;
    }

    public String getType() { return type; }

    public Pair<Long, Long> getLocation() { return location; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; location = new Pair<>(0L, 0L);}

    public void setLocation(Pair<Long, Long> location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
