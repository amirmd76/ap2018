import org.json.JSONObject;

public class Player implements GamePlayer {
    Account account;
    String playerName, Directory = "";
    Map map;
    int time = 0;

    public Player(String playerName) {
        this.account = new Account(Constants.Initial_Player_Money);
        this.playerName = playerName;
        map = new Map(Constants.MAP_HEIGHT, Constants.MAP_WIDTH);
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("account", account.dump());
        object.put("playerName", playerName);
        object.put("Directory", Directory);
        object.put("map", map.dump());
        object.put("time", time);
        return object;
    }

    public Player(JSONObject object) {
        account = new Account(object.getJSONObject("account"));
        playerName = object.getString("playerName");
        Directory = object.getString("Directory");
        map = new Map(object.getJSONObject("map"));
        time = object.getInt("time");
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

    @Override
    public String getDirectory() {
        return Directory;
    }

    public String update(int time) {
        this.time = time;
        return map.update(time);
    }
}
