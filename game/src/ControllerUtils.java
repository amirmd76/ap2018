import java.util.HashMap;

public class ControllerUtils {
    private HashMap<String, Integer> ids = new HashMap<>();
    public int getID(String s) {
        if(!ids.containsKey(s)) {
            ids.put(s, 2);
            return 1;
        }
        int id = ids.get(s);
        ids.put(s, id + 1);
        return id;
    }
}
