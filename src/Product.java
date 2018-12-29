public class Product {

    Pair<Long, Long> location;         // Location on the map [x,y]
    String type;
    boolean inStorage, Destroyed;
    private final int size;

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

    public String getType() { return type; }

    public Pair<Long, Long> getLocation() { return location; }

    public int getSize() { return size; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; location = new Pair<>(0L, 0L);}

    public void setLocation(Pair<Long, Long> location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
