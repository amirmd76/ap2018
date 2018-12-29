import java.util.ArrayList;
import java.util.HashMap;

public class Workshop implements UpgradeableObject {

    private String type;
    private int level, timeOfProduce;               //TODO handling Producing time in class constants
    private String inputType;
    private String returnType;
    private HashMap<String, ArrayList<Product>> products;           //TODO CHeck logic of this argument
    private int ID;

    public int getID() {
        return ID;
    }

    public Workshop(int ID, String type) {
        this.ID = ID;
        this.type = type;
        this.inputType = Constants.WORKSHOP_INPUT_TYPES.get(type);
        this.returnType = Constants.WORKSHOP_RETURN_TYPES.get(type);
        this.level = 1;
    }

    public String getType() {
        return type;
    }

    public String produce(Storage storage){        // produce a product with the count of level!
        for (ArrayList<>)
        try {
            storage.store(inputType, -1 * level);
        }
        catch (Exception e){        //TODO handle exception if there is not enough of input type in storage
            return e.toString();
        }
        return String.format("%s workshop has produced %d %s", this.type, this.level, this.returnType);
    }

    public String store(Storage storage, ArrayList<Product> items){
        try {
            storage.store(, items.size());
        }
        catch (Exception e){
            return e.toString();
        }
        return String.format("%s workshop has produced %d %s", this.type, this.level, this.returnType);
    }

    @Override
    public int getLevel() {
        return level;
    }
}
