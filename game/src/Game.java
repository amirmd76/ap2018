import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    static ArrayList<Controller> controllers = new ArrayList<>();
    static ArrayList<Level> levels = new ArrayList<>();
    static long time = 0;
    static int currentPlayer = 0;
    static Event event = new Event();
    static Thread server;
    static Thread client;
    static Shop shop = new Shop();
    private HashMap<String, Long> boughtItems = new HashMap<>();
    private HashMap<String, Long> soldItems = new HashMap<>();
    public ArrayList<String> commands = new ArrayList<>();
    private String IDERROR = null, friendNotification = null;
    public HashMap<String, Player> connectedPlayers = new HashMap<>();
    public ArrayList<String> chatRoom = new ArrayList<>();
    public ArrayList<String> privateChat = new ArrayList<>();
    public ArrayList<String> textInput = new ArrayList<>();
    public ArrayList<String> privateTextInput = new ArrayList<>();
    public boolean isMultiPlayer = false;

    public static JSONObject dump() {
        JSONObject object = new JSONObject();
        try {
            object.put("time", time);
            //        object.put("levels", levels.toArray());
            JSONArray array = new JSONArray();
            for (Controller controller : controllers)
                array.put(controller.dump());
            object.put("controllers", array);
            object.put("currentPlayer", currentPlayer);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void load(JSONObject object) {
        try {
            time = object.getInt("time");
            currentPlayer = object.getInt("currentPlayer");
//        JSONArray array = object.getJSONArray("levels");
//        for(int i = 0; i < array.length(); ++ i)
//            levels.add(array.getString(i));
            JSONArray array = null;
            array = object.getJSONArray("controllers");
            for (int i = 0; i < array.length(); ++i)
                controllers.add(new Controller(array.getJSONObject(i), event));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame() throws Exception {
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

    public static void saveGame() throws Exception {
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
    }

    public Controller getCurrentController() {
        return controllers.get(currentPlayer);
    }

    public void menu(Menu menu, JFrame frame, NewGame newGame, UI ui ){
        String action = null;
        MultiPlayer multiPlayer = null;
        HostStart hostStart = null;
        ClientStart clientStart = null;
        while (true){
            if (!menu.getAction().equals("NoAction")){
                action = menu.getAction();
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int actionCount = 0;
        String nickName = null;
        System.out.println(action);
        switch (action){                //TODO
            case "New Game":{                   //TODO add a back button
                frame.getContentPane().removeAll();
                newGame = new NewGame(400, 300);
                frame.getContentPane().add(newGame);
                frame.setResizable(true);
                frame.pack();
                actionCount = 1;
                break;
            }
            case "Load Game":{ //TODO handle loading games by Nickname of players(sorted by player ID)
                break;
            }
            case "MultiPlayer": {        //TODO handle this first on level class
                frame.getContentPane().removeAll();
                isMultiPlayer = true;
                multiPlayer= new MultiPlayer(800, 600);
                frame.getContentPane().add(multiPlayer);
                frame.setResizable(true);
                frame.pack();
                actionCount = 4;
                break;
            }
        }
        if (actionCount == 1){
            while (true){
                //System.out.println("While2");
                if (!newGame.getAction().equals("NoAction"))
                    break;
                try {
                    TimeUnit.MILLISECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nickName = newGame.getNickname();
            System.out.println(nickName);
            controllers.get(currentPlayer).player.setNameID(nickName);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(ui);
            frame.pack();
        }

        if (actionCount == 4){
            while (true){
                if (!multiPlayer.getAction().equals("NoAction"))
                    break;
                try {
                    TimeUnit.MILLISECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String cmd = multiPlayer.getAction();
            switch (cmd){
                case "Host":{
                    frame.getContentPane().removeAll();
                    hostStart = new HostStart(400, 300);
                    frame.getContentPane().add(hostStart);
                    frame.pack();
                    while(true){
                        if(!hostStart.getAction().equals("NoAction")){
                            break;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    server = new Thread(new Server(hostStart.getPort(), shop, connectedPlayers, chatRoom, commands, privateChat, textInput, privateTextInput));
                    server.start();
                    break;
                }
                case "Client":{
                    frame.getContentPane().removeAll();
                    clientStart = new ClientStart(400, 500);
                    frame.getContentPane().add(clientStart);
                    frame.pack();
                    while(true){
                        if(!clientStart.getAction().equals("NoAction")){
                            break;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    client = new Thread(new Client(clientStart.getPort(), clientStart.getIP(), controllers.get(currentPlayer).player, commands, shop, boughtItems, soldItems, IDERROR, friendNotification, connectedPlayers, chatRoom, privateChat, textInput, privateTextInput));
                    client.start();
                    break;
                }
            }
        }

    }

    public void inGameMenu(InGameMenu inGameMenu, Menu menu, JFrame frame, NewGame newGame, UI ui){
        while (inGameMenu.getAction().equals("NoAction")) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(inGameMenu.getAction());
        switch (inGameMenu.getAction()){
            case "Continue":{
                frame.getContentPane().remove(inGameMenu);
                frame.pack();
                break;
            }
            case "Restart": {   //TODO Handling restarting a level
                break;
            }
            case "Return":{
                frame.getContentPane().removeAll();
                frame.getContentPane().add(menu);
                frame.pack();
                menu(menu, frame, newGame, ui);
                break;
            }
        }
    }

    public static void main(String[] args) {
        (new Game()).run();
    }

    public void run(){
        JFrame frame = new JFrame();
        Menu menu = new Menu(800, 600);
        UI ui = new UI(this, 1200, 900);
        InGameMenu inGameMenu = new InGameMenu();
        NewGame newGame = new NewGame();
           /* frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(menu);
            frame.setResizable(false);
            frame.pack();*/

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(menu);
        //frame.getContentPane().add(inGameMenu);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        boolean b = true;

        menu(menu, frame, newGame, ui);


        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            String command = sc.nextLine();
            runCommand(command);
        }
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