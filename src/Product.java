public class Product {

    private int[] location = new int[2];         // Location on the map [x,y]   TODO make this location compatible with map
    private String type;
    private boolean inStorage, Destroyed;
    private final int size;

    public Product(int[] location, String type) {
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

    public int getSize() { return size; }

    public int[] getLocation() { return location; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; location = new int[]{0,0};}

    public void setLocation(int[] location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
