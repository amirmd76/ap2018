import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    ArrayList<Controller> controllers = new ArrayList<>();
    ArrayList<String> levels = new ArrayList<>();
    long time = 0;
    int currentPlayer = 0;

    public void addLevel(String level) {
        levels.add(level);
    }

    public void load(String json) {}
    public String save(){
        return "";
    }

    public void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String command = sc.nextLine();
            String[] words = command.split(" ");
            StringBuilder cmd = new StringBuilder();
            for(int i = 0; i < words.length; i ++)
                cmd.append(words[i]).append(" ");
            Controller controller = controllers.get(currentPlayer);
            String x = words[0];
            if(x.equals("buy"))
                controller.add(cmd.toString());
            if(x.equals("pickup"))
                controller.pickup(cmd.toString());
            if(x.equals("cage"))
                controller.cage(cmd.toString());
            if(x.equals("plant"))
                controller.plant(cmd.toString());
            if(x.equals("well"))
                controller.well(cmd.toString());
            if(x.equals("upgrade"))
                controller.upgrade(cmd.toString());
            if(x.equals("start"))
                controller.produce(cmd.toString());
            if(x.equals("print")); //info/map/levels/warehouse/well/workshop/truck/helicopter
            if(x.equals("turn"))
                controller.turn(cmd.toString());
        }
    }
}
