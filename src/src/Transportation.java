import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class FullException extends Exception {
    public FullException(String message) {
        super(message);
    }
}

class Type_Mismatch extends Exception {
    public Type_Mismatch(String message) {
        super(message);
    }
}

class Box {
    private int capacity = 20;
    private int left_Capacity;
    private String type;
    private ArrayList<Product> products = new ArrayList<>();
    private int items_Count;

    public Box(String type){
        this.type = type;
        this.left_Capacity = capacity;
        this.items_Count =0;
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("capacity", capacity);
        object.put("leftCapacity", left_Capacity);
        object.put("type", type);
        object.put("itemsCount", items_Count);
        JSONArray array = new JSONArray();
        for(Product product: products)
            array.put(product.dump());
        object.put("products", array);
        return object;
    }

    public Box(JSONObject object) {
        capacity = object.getInt(("capacity"));
        left_Capacity = object.getInt("leftCapacity");
        type = object.getString("type");
        items_Count = object.getInt("itemsCount");
        JSONArray array = object.getJSONArray("products");
        for(Object obj: array)
            products.add(new Product((JSONObject)obj));
    }

    public ArrayList<Product> getProducts() { return products; }

    public int getItemsCount() { return items_Count; }

    public String getType() { return type; }

    public int addToBox(Product item) throws Exception{
        if (!(item.getType().equals(type))){
            throw new Type_Mismatch("Items type do not match");
        }
        if (left_Capacity == 0)
            throw new FullException("Box is full");
        if ((left_Capacity - item.getSize()) > 0){
            products.add(item);
            left_Capacity -= item.getSize();
            items_Count++;
        }
        return items_Count;
    }
}

public class Transportation implements UpgradeableObject {
    private final String type;
    private long box_Count_Capacity;
    private ArrayList<Box> boxes = new ArrayList<>();
    private int level, travel_Duration;

    public Transportation(String type) {
        this.type = type;
        switch (this.type) {
            case "truck" : {
                this.box_Count_Capacity = Constants.Truck_Initial_Box_Capacity;
                this.travel_Duration = Constants.Truck_Initial_Travel_Duration;
                break;
            }
            case "helicopter" : {
                this.box_Count_Capacity = Constants.Helicopter_Initial_Box_Capacity;
                this.travel_Duration = Constants.Helicopter_Initial_Travel_Duration;
                break;
            }
        }
        level = 1;
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.
    }

    public String getType() { return type; }

    public int getTravelDuration() { return travel_Duration; }

    public String sell(Storage storage, String type, ArrayList<Product> items){                //TODO Handle money in account
        if(items.isEmpty())
            return "Truck is empty";
        ArrayList<Product> storedItems = new ArrayList<>();
        int count = 0, i;                                                              //Selling products of one type
        i = getBox(type);
        if (i >= box_Count_Capacity)
            return ("Truck is full");
        String str;
        str = storage.store(type, -items.size(), items.get(0).getSize());                  //TODO make Storage class compatible with Product class
        if (str.equals("ok"))
            storage.store(type, items.size(), items.get(0).getSize());
        else
            return String.format("There is not enough %s in the storage!", type);
        try {                                                //For selling many types this method should be called for individual type of products
            for (Product items2 : items){
                boxes.get(i).addToBox(items2);
                storedItems.add(items2);
                count++;
            }
        }        //Command would be: sell product_Type1 Count1, sell product_Type2 Count2... at the end selling will be done by command "sell" with no argument
        catch (FullException e){
            if (count == 0){
                if(count < items.size()) {
                    ArrayList<Product> notStoredItems = new ArrayList<>();
                    for(Product product: items) {
                        if(!storedItems.contains(product))
                            notStoredItems.add(product);
                    }
                    sell(storage, type, notStoredItems);
                }
                return e.toString();
            }
        }
        catch (Exception e){
            return e.toString();
        }
        finally {
            return String.format("Truck is ready for selling %d of type %s", count, type);
        }
    }

    public String completeSelling(Storage storage, Account account){
        StringBuilder str = new StringBuilder();
        for (Box box: boxes){
            storage.store(box.getType(), -box.getItemsCount(), 0);
            for(int i = 0; i < box.getItemsCount(); i ++)
                try {
                    account.spend("sell", box.getType(), true);
                }
                catch (Exception ignore){}
            str.append(String.format("%d of %s have been sold /n", box.getItemsCount(), box.getType()));
        }
        removeBoxesAtTheEndOfWork();
        return str.toString();
    }

    public String buy(String type, long count){                 //TODO Handle money in account
        int i = 0, counter = 0;                                                              //Selling products of one type
        ArrayList<Product> items = new ArrayList<>();
        i = getBox(type);
        if (i >= box_Count_Capacity)
            return ("Helicopter is full");
        for (int j = 0; j < count; j++){
            items.add(new Product(new Pair<>(0L, 0L),type));
        }       //Command would be: buy product_Type1 Count1, buy product_Type2 Count2... at the end selling will be done by command "buy" with no argument
        try{
            for (Product items2 : items){
                boxes.get(i).addToBox(items2);
                counter++;
            }
        }
        catch (FullException e){
            if (counter == 0){
                return e.toString();
            }
            else if(count < items.size())
                buy(type, count - counter);
        }
        catch (Exception e){
            return e.toString();
        }
        finally {
            return String.format("Helicopter is ready for buying %d of %s", counter, type);
        }
    }

    public Pair<ArrayList<Product>, String> completeBuying(){
        StringBuilder str = new StringBuilder();
        ArrayList<Product> products = new ArrayList<>();
        for (Box box : boxes){
            String type = box.getType();
            int count = box.getItemsCount();
            str.append(String.format("%d %s have been bought \n", box.getItemsCount(), box.getType()));
            while(count -- > 0) {
                Product product = new Product(null, type);
                products.add(product);
            }
        }
        removeBoxesAtTheEndOfWork();
        return new Pair<>(products, str.toString());
    }

    public String upgrade(){
        switch (type){
            case "truck": {
                try {
                    if (level == Constants.Truck_Max_Level_Upgrade)
                        throw new Exception("Truck is fully upgraded");
                    else {
                        level ++;
                        box_Count_Capacity++;
                        travel_Duration -= Constants.Truck_Upgrade_Travel_Duration;
                        return String.format("Truck is now upgraded to level %d", level);
                    }
                }
                catch (Exception e){
                    return e.toString();
                }
            }
            case "helicopter": {
                try {
                    if (level == Constants.Helicopter_Max_Level_Upgrade)
                        throw new Exception("Helicopter is fully upgraded");
                    else {
                        level ++;
                        box_Count_Capacity++;
                        travel_Duration -= Constants.Helicopter_Upgrade_Travel_Duration;
                        return String.format("Helicopter is now upgraded to level %d", level);
                    }
                }
                catch (Exception e){
                    return e.toString();
                }
            }
            default:
                return null;
        }
    }

    public void removeBoxesAtTheEndOfWork(){   //This method should be called at the end of selling and buying commands to delete boxes from vehicles
        boxes.clear();
    }

    public int getBox(String type){
        int i = 0;
        if (boxes.size() == 0)
            boxes.add(new Box(type));
        else {
            for (Box box1 : boxes) {
                if (box1.getType().equals(type))
                    break;
                i++;
            }
        }
        return i;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
