public class Player {
    Account account;
    String player_Name;

    public Player(String player_Name) {
        this.account = new Account(Constants.Initial_Player_Money);
        this.player_Name = player_Name;
    }
}
