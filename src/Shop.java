import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Shop implements Serializable {
    HashMap<String, Long> products = new HashMap<>();
    HashMap<String, Long> wilds = new HashMap<>();

    public Shop() {
            wilds.put("lion", 10L);
            wilds.put("bear", 10L);
            products.put("egg", 10L);       //TODO complete this constructor
    }

    public void sell(String name, Long number){
        Set<String> keys = products.keySet();
        if (keys.contains(name)){
            Long count = products.get(name);
            count -= number;
            products.put(name, count);
        }
        else{
            Long count = wilds.get(name);
            count -= number;
            wilds.put(name, count);
        }
    }

    public void bought(String name, Long number){
        Set<String> keys = products.keySet();
        if (keys.contains(name)){
            Long count = products.get(name);
            count += number;
            products.put(name, count);
        }
        else{
            Long count = wilds.get(name);
            count += number;
            wilds.put(name, count);
        }
    }
}
