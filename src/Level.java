import java.io.StringWriter;
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

    public int getLevelID() {
        return levelID;
    }

    public String print() {
        StringBuilder ans = new StringBuilder(String.format("Level %d has %n goals:\n", levelID, goals.size()));
        for(String key: goals.keySet())
            ans.append(String.format("- Having %d of %s\n", goals.get(key), key));
        return ans.toString();
    }

    public boolean hasReachedGoals(Controller controller){
        long Money = controller.player.account.getMoney();
        boolean reachedGoal = true;
        for (String i : goals.keySet()){
            switch (i){
                case "money" : { if (Money != goals.get(i)) return false; }
                case "egg": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "milk": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "wool": { if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "flour": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "cake": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "eggPowder": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "cookie": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "feather": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "cotton": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "string": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "cloth": {if (controller.player.map.getProducts_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "turkey": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "cow": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                case "sheep": {if (controller.player.map.getAnimals_by_Type(i).size() != goals.get(i)) reachedGoal = false; break;}
                default : { reachedGoal = true;}
            }
        }
        return reachedGoal;
    }
}
