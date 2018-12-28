public class Workshop {

    private String type;
    private int level, timeOfProduce;               //TODO handling Producing time in class constants
    private String inputType;
    private String returnType;

    public Workshop(String type, String inputType, String returnType) {
        this.type = type;
        this.inputType = inputType;
        this.returnType = returnType;
        this.level = 1;
    }

    public String produce(Storage storage){        // produce a product with the count of level!
        try {
            storage.remove(inputType, level);
        }
        catch (Exception e){        //TODO handle exception if there is not enough of input type in storage
            return e.toString();
        }
        return String.format("%s workshop has produced %d %s", this.type, this.level, this.returnType);
    }
}
