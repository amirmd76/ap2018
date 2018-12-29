public class Player implements GamePlayer {
    Account account;
    String playerName, Directory = "";
    Map map;
    int time;

    public Player(String playerName) {
        this.account = new Account(Constants.Initial_Player_Money);
        this.playerName = playerName;
        map = new Map(Constants.MAP_HEIGHT, Constants.MAP_WIDTH);
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
