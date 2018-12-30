import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {
    private static HashMap<Pair<String, String>, Long> costs = new HashMap<>(); // TODO: fill
    private long money;

    public Account() {money = 0;}
    public Account(long money) {this.money = money;}

    public JSONObject dump() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("money", money);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public String print() {
        return String.format("You have %d units of money", money);
    }

    public Account(JSONObject data) {
        try {
            money = data.getLong("money");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public long getMoney() {
        return money;
    }

    public long getCost(String name, String type) {
        return costs.get(new Pair<>(name, type));

    }

    public String spend(ArrayList<Pair<String, String>> actions, boolean apply) throws NotFoundException, NotEnoughMoneyException {
        StringBuilder res = new StringBuilder();
        long currentMoney = money;
        for(Pair<String, String> action: actions) {
            if (!costs.containsKey(action))
                throw new NotFoundException("Cost not found");
            long cost = costs.get(action);
            if (cost > currentMoney)
                throw new NotEnoughMoneyException("Not enough money to spend");
            currentMoney -= cost;
            if(apply)
                res.append(String.format("Spent %d on %s %s", cost, action.x, action.y));
        }
        if(apply)
            money = currentMoney;
        return res.toString();
    }

    public String spend(String name, String type, boolean apply) throws NotFoundException, NotEnoughMoneyException {
        Pair<String, String> key = new Pair<>(name, type);
        ArrayList<Pair<String, String>> actions = new ArrayList<>();
        actions.add(key);
        return spend(actions, apply);
    }

    public String spend(String name, long cost, boolean apply) throws NotEnoughMoneyException {
        long currentMoney = money;
        if (cost > currentMoney)
            throw new NotEnoughMoneyException("Not enough money to spend");
        currentMoney -= cost;
        if(apply)
            money = currentMoney;
        if(apply)
            return String.format("Spent %d on %s", cost, name);
        return "";
    }

    public boolean checkBuyItem(String type) {
        try {
            spend(type, "buy", false);
        }
        catch (Exception ignore) {
            return false;
        }
        return true;
    }

    public void buyItem(String type) {
        try {
            spend(type, "buy", true);
        }
        catch (Exception ignore) {}
    }

    public boolean checkUpgradeItem(String type, long level) {
        String lvl = Long.toString(level);
        String action = "upgrade_" + lvl;
        try {
            spend(type, action, false);
        }
        catch (Exception ignore) {
            return false;
        }
        return true;
    }

    public void upgradeItem(String type, long level) {
        String lvl = Long.toString(level);
        String action = "upgrade_" + lvl;
        try {
            spend(type, action, true);
        }
        catch (Exception ignore) {}
    }

    public boolean checkRequest(ArrayList<String> items, boolean sell) {
        ArrayList<Pair<String, String>> actions = new ArrayList<>();
        String type;
        if(sell)
            type = "sell_product";
        else
            type = "buy_product";
        for(String item: items)
            actions.add(new Pair<>(item, type));
        try {
            spend(actions, false);
        }
        catch (Exception ignore) {
            return false;
        }
        return true;
    }

    public void doRequest(ArrayList<String> items, boolean sell) {
        ArrayList<Pair<String, String>> actions = new ArrayList<>();
        String type;
        if(sell)
            type = "sell_product";
        else
            type = "buy_product";
        for(String item: items)
            actions.add(new Pair<>(item, type));
        try {
            spend(actions, true);
        }
        catch (Exception ignore) {}
    }

}