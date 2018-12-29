import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Workshop implements UpgradeableObject {

    private String type;
    private int level, timeOfProduce, maxLevel;               //TODO handling Producing time in class constants
    private String[] inputType;
    private String returnType;
    private Long[] inputTypeSizes;
    private int ID;

    public int getID() {
        return ID;
    }

    public Workshop(int ID, String type) {
        this.ID = ID;
        this.type = type;
        this.inputType = Constants.WORKSHOP_INPUT_TYPES.get(type);
        this.returnType = Constants.WORKSHOP_RETURN_TYPES.get(type);
        this.inputTypeSizes = Constants.WORKSHOP_INPUT_SIZES.get(type);
        this.level = 1;
    }

    public String print() {
        StringBuilder ans = new StringBuilder();
        ans.append(String.format("Workshop of type %s (id = %d, level = %d), input = ", type, ID, level));
        for(int i = 0; i < inputType.length; ++ i)
            ans.append(String.format("%d of %s, ", inputTypeSizes[i], inputType[i]));
        ans.append(String.format("and produces %s\n", returnType));
        return ans.toString();
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("level", level);
        object.put("timeOfProduce", timeOfProduce);
        object.put("maxLevel", maxLevel);
        object.put("inputType", inputType);
        object.put("returnType", returnType);
        object.put("inputTypeSizes", inputTypeSizes);
        object.put("ID", ID);
        return object;
    }

    public Workshop(JSONObject object) {
        type = object.getString("type");
        level = object.getInt("level");
        timeOfProduce = object.getInt("timeOfProduce");
        maxLevel = object.getInt("maxLevel");
        JSONArray inputType = object.getJSONArray("inputType");
        this.inputType = new String[inputType.length()];
        for(int i = 0; i < inputType.length(); ++ i)
            this.inputType[i] = inputType.getString(i);
        JSONArray inputTypeSizes = object.getJSONArray("inputTypeSizes");
        this.inputTypeSizes = new Long[inputTypeSizes.length()];
        for(int i = 0; i < inputTypeSizes.length(); i ++)
            this.inputTypeSizes[i] = inputTypeSizes.getLong(i);
        returnType = object.getString("returnType");
        ID = object.getInt("ID");
    }


    public String getType() {
        return type;
    }

    public Pair<ArrayList<Product>, String> produce(Storage storage){        // produce a product with the max count of level!
        int inputCount = inputType.length, returnCount = level;
        int[] count = new int[inputCount];
        ArrayList<Product> items = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            count [i] = 0;
            for (int j = level; j >0 ; j--){
                String str = storage.store(inputType[i], -1 * j, inputTypeSizes[i]);
                if (str.equals("ok")) {
                    count[i] = j;
                    break;
                }
            }
            if (count[i] == 0)
                return new Pair<>(null, String.format("There is not enough of %s in the storage", inputType[i]));
        }
        for (int aCount : count) {
            if (aCount < returnCount)
                returnCount = aCount;
        }
        for (int i = 0; i < count.length; i++){
            if (count[i] > returnCount)
                storage.store(inputType[i], count[i] - returnCount, inputTypeSizes[i]);
        }
        for (int i = 0; i < returnCount; i++)
            items.add(new Product(null, returnType));

        return new Pair<>(items,String.format("%s%d workshop has produced %d %s", this.type,this.ID, returnCount, this.returnType));
    }

    public String upgrade(){
        if (level ==  maxLevel)
            return String.format("%s%d workshop is fully upgraded", this.type, this.ID);
        level++;
        timeOfProduce--;            //TODO handle time of production in constants
        return String.format("%s%d has upgraded to level %d", this.type, this.ID, this.level);
    }

    @Override
    public int getLevel() {
        return level;
    }
}
