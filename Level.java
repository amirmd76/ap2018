import java.util.HashMap;
import java.util.Set;

public class Level {
    HashMap<String, Long> goals = new HashMap<>();
    int levelID;

    public Level(HashMap<String, Long> goals, int levelID) {
        this.goals = goals;
        this.levelID = levelID;
    }

    public Level() {
        this.levelID = 1;
        goals.put("egg", 10L);
        goals.put("turkey", 2L);
        goals.put("money", 1000L);
    }

    public boolean hasReachedGoals(Controller controller){
        long Money = controller.player.account.getMoney();
        boolean reachedGoal = true;
        for (String i : goals.keySet()){
            switch (i){
                case "money" : { if (Money != goals.get(i)) return false; }
                case "egg": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "milk": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "wool": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "flour": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "cake": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "eggpowder": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "cookie": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "feather": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "cotton": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "string": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "cloth": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "turkey": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "cow": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                case "sheep": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false;}
                default : { reachedGoal = true;}
            }
        }
        return reachedGoal;
    }
}
