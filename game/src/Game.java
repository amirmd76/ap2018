import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    ArrayList<Controller> controllers = new ArrayList<>();
    ArrayList<Level> levels = new ArrayList<>();
    long time = 0;
    int currentPlayer = 0;
    Event event = new Event();

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("time", time);
//        object.put("levels", levels.toArray());
        JSONArray array = new JSONArray();
        for(Controller controller: controllers)
            array.put(controller.dump());
        object.put("controllers", array);
        object.put("currentPlayer", currentPlayer);
        return object;
    }

    public void load(JSONObject object) {
        time = object.getInt("time");
        currentPlayer = object.getInt("currentPlayer");
//        JSONArray array = object.getJSONArray("levels");
//        for(int i = 0; i < array.length(); ++ i)
//            levels.add(array.getString(i));
        JSONArray array = object.getJSONArray("controllers");
        for(int i = 0; i < array.length(); ++ i)
            controllers.add(new Controller(array.getJSONObject(i), event));
    }

    public void loadGame() throws Exception {
        controllers.clear();
        File file = new File(Constants.DUMP_FILE);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");
        JSONObject obj = new JSONObject(str);
        load(obj);
    }
    public void saveGame() throws  Exception{
        File file = new File(Constants.DUMP_FILE);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        JSONObject obj = dump();
        obj.write(writer);
        writer.flush();
        writer.close();
    }

    public Game() {
        controllers.add(new Controller(event, "guest"));
        levels.add(new Level());
        runCommand("buy chicken"); // TODO: REMOVE
        runCommand("buy chicken");
        runCommand("buy turkey");
        runCommand("buy cow");
        runCommand("buy sheep");
        runCommand("buy cat");
        runCommand("buy dog");
        runCommand("buy dog");
        runCommand("buy dog");
        runCommand("buy dog");
        runCommand("buy dog");
        runCommand("buy dog");



    }

    public static void main(String[] args) {
        (new Game()).run();
    }


     public void run() {
         JFrame frame = new JFrame();

         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.getContentPane().add(new UI(this, 1200, 900));
         frame.setResizable(false);
         frame.pack();

         frame.setLocationByPlatform(true);
         frame.setVisible(true);

        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String command = sc.nextLine();
            runCommand(command);
        }
    }

    public Controller getCurrentController() {
        return controllers.get(currentPlayer);
    }

    public void runCommand(String command) {
        String[] words = command.split("\\s+");
        StringBuilder cmd = new StringBuilder();
        for(int i = 1; i < words.length; i ++) {
            cmd.append(words[i]);
            if(i + 1 < words.length)
                cmd.append(" ");
        }
        Controller controller = controllers.get(currentPlayer);
        String x = words[0];
        switch (x) {
            case "buy":
                controller.add(cmd.toString());
                break;
            case "pickup":
                controller.pickup(cmd.toString());
                break;
            case "cage":
                controller.cage(cmd.toString());
                break;
            case "plant":
                controller.plant(cmd.toString());
                break;
            case "well":
                controller.well(cmd.toString());
                break;
            case "upgrade":
                controller.upgrade(cmd.toString());
                break;
            case "start":
                controller.produce(cmd.toString());
                break;
            case "print":
                if (words[1].equals("levels")) {
                    for(Level level : levels)
                        event.printStatus(level.print());
                    break;
                }
                event.printStatus(controller.print(cmd.toString()));
                break;
            case "turn":
                for(Level level: levels)
                    if(level.hasReachedGoals(controller))
                        event.printStatus(String.format("YOU HAVE COMPLETED LEVEL %d !!!!!!!!", level.getLevelID()));
                controller.turn(cmd.toString());
                break;
            case "save":
                try {
                    saveGame();
                    event.printStatus(String.format("Game saved to %s", Constants.DUMP_FILE));
                }
                catch (Exception e) {
                    event.printStatus(e.getMessage());
                }
                break;
            case "load":
                try {
                    loadGame();
                    event.printStatus("Game loaded successfully");
                }
                catch (Exception e) {
                    event.printStatus(e.getMessage());
                }
            case "add_player":
                String playerName = words[1];
                controllers.add(new Controller(event, playerName));
                currentPlayer = controllers.size() - 1;
                event.printStatus("Player " + playerName + " created");
                break;
            case "switch_player":
                playerName = words[1];
                boolean flag = false;
                for(int i = 0; i < controllers.size(); ++ i)
                    if(controllers.get(i).player.getName().equals(playerName)) {
                        currentPlayer = i;
                        flag = true;
                        break;
                    }
                if(!flag)
                    event.printStatus("Player not found");
                else
                    event.printStatus("Switched to player " + playerName);
                break;
        }
    }
}
