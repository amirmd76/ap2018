import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    ArrayList<Controller> controllers = new ArrayList<>();
    ArrayList<String> levels = new ArrayList<>();
    long time = 0;

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
            // TODO: implement
        }
    }
}
