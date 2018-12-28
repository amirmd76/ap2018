import java.util.ArrayList;

public interface MapCell {
    boolean isBlocked();
    ArrayList<String> getItems();
    void clearItems();
}
