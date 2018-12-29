public class Product {

    private Pair<Long, Long> location = new Pair<>();         // Location on the map [x,y]   TODO make this location compatible with map
    private String type;
    private boolean inStorage, Destroyed;
    private final int size;

    public Product(long x, long y, String type) {
        this.location = new Pair<>(x, y);
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

    public String getType() { return type; }

    public int getSize() { return size; }

    public Pair<Long, Long> getLocation() { return location; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; new Pair<>(0, 0);}

    public void setLocation(Pair<Long, Long> location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
