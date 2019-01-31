import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements GamePlayer, Serializable {
    Account account;
    String playerName, Directory = "", nameID;
    ArrayList<String> friends = new ArrayList<>();
    long deals, multiPlayerGames;
    Map map;
    int time = 0;
    HashMap<String, Long> boughtWilds = new HashMap<>();

    public Player(String playerName, String ID) {
        this.account = new Account(Constants.Initial_Player_Money);
        this.playerName = playerName;
        map = new Map(Constants.MAP_HEIGHT, Constants.MAP_WIDTH);
        this.nameID = ID;
        deals = 0;
        multiPlayerGames = 0;
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        try {
            object.put("account", account.dump());
            object.put("playerName", playerName);
            object.put("Directory", Directory);
            object.put("map", map.dump());
            object.put("time", time);
            object.put("deals", deals);
            object.put("multiPlayerGames", multiPlayerGames);
            object.put("friends", friends);
            object.put("nameID", nameID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Player(JSONObject object) {
        try {
            account = new Account(object.getJSONObject("account"));
            playerName = object.getString("playerName");
            Directory = object.getString("Directory");
            map = new Map(object.getJSONObject("map"));
            time = object.getInt("time");
            deals = object.getLong("deals");
            multiPlayerGames = object.getLong("multiPlayerGames");
            friends = (ArrayList<String>) object.get("friends");
            nameID = object.getString("nameID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public String getName() {
        return playerName;
    }

    public String getNameID() {
        return nameID;
    }

    public HashMap<String, Long> getBoughtWilds() {
        return boughtWilds;
    }

    @Override
    public String getDirectory() {
        return Directory;
    }

    public String update(int time) {
        this.time = time;
        return map.update(time);
    }
}
