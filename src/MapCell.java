import java.util.ArrayList;

public interface MapCell {
    boolean isBlocked();
    ArrayList<Product> getProducts();
    void clearProducts();
}