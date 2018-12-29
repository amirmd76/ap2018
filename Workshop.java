import java.util.ArrayList;
import java.util.HashMap;

public class Workshop {

    private String type;
    private int level, timeOfProduce, maxLevel;               //TODO handling Producing time in class constants
    private String[] inputType;
    private String returnType;
    private long[] inputTypeSizes;
    private long returnTypeSize;
    protected int ID;

    public Workshop(String type, String[] inputType, String returnType, int ID) {
        this.type = type;
        this.inputType = inputType;
        this.returnType = returnType;
        this.level = 1;
        this.ID = ID;
    }

    public Workshop(String type, int ID) {
        this.type = type;
        this.inputType = Constants.WORKSHOP_INPUT_TYPES.get(type);
        this.returnType = Constants.WORKSHOP_RETURN_TYPES.get(type);
        this.level = 1;
        this.ID = ID;
    }

    public Pair<ArrayList<Product>, String> produce(Storage storage){        // produce a product with the max count of level!
        int inputCount = inputType.length, returnCount = level;
        int[] count = null;
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
        for (int i = 0; i < count.length; i++){
            if (count[i] < returnCount)
                returnCount = count[i];
        }
        for (int i = 0; i < count.length; i++){
            if (count[i] > returnCount)
                storage.store(inputType[i], count[i] - returnCount, inputTypeSizes[i]);
        }
        for (int i = 0; i < returnCount; i++)
            items.add(new Product(0 , 0, returnType));

        return new Pair<>(items,String.format("%s%d workshop has produced %d %s", this.type,this.ID, returnCount, this.returnType));
    }

    public Pair<Boolean, String> upgrade(){
        if (level ==  maxLevel)
            return new Pair<>(false, String.format("%s%d workshop is fully upgraded", this.type, this.ID));
        level++;
        timeOfProduce--;            //TODO handle time of production in constants
        return new Pair<>(true, String.format("%s%d has upgraded to level %d", this.type, this.ID, this.level));
    }
}
