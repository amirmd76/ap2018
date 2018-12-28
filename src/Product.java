public class Product {

    int[] location = new int[2];         // Location on the map [x,y]
    String type;
    boolean inStorage, Destroyed;

    public Product(int[] location, String type) {
        this.location = location;
        this.type = type;
        inStorage = false;
        Destroyed = false;
    }

    public String getType() { return type; }

    public int[] getLocation() { return location; }

    public boolean isDestroyed() { return Destroyed; }

    public boolean isInStorage() { return inStorage; }

    public void setInStorage() { this.inStorage = true; location = new int[]{0,0};}

    public void setLocation(int[] location) { this.location = location; }

    public void destroy (){ this.Destroyed = true; }
}
