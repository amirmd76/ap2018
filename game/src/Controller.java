import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    Player player;
    Transportation truck = new Transportation("Truck"),
            helicopter = new Transportation("Helicopter");
    Event event;
    ControllerUtils utils = new ControllerUtils();
    int time = 0;

    Controller(Event event, String playerName) {
        this.event = event;
        player = new Player(playerName);
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("player", player.dump());
        object.put("truck", truck.dump());
        object.put("helicopter", helicopter.dump());
        object.put("time", time);
        return object;
    }

    public Controller(JSONObject object, Event event) {
        this.event = event;
        player = new Player(object.getJSONObject("player"));
        truck = new Transportation(object.getJSONObject("truck"));
        helicopter = new Transportation(object.getJSONObject("helicopter"));
        time = object.getInt("time");
    }

    public Pair<Long, Long> addAnimalToMap(Map map, Animal animal) {
        Pair<Long, Long> loc = map.getRandomCell();
        animal.setDirection(map.getRandomDirection());
        map.getCell(loc).addAnimal(animal);
        animal.setLocation(loc);
        if(animal.getType().equals("Cat"))
            map.cat = animal.getId();
        else if(animal.getType().equals("Dog"))
            map.dog = animal.getId();
        event.printStatus(String.format("A new %s added to (%d, %d)", animal.getType().toLowerCase(), loc.x, loc.y));
        return loc;
    }

    public String print(String command) {
        StringBuilder ans = new StringBuilder();
        switch (command) {
            case "info":
                ans.append(player.getAccount().print());
                ans.append(player.getMap().print());
                ans.append(player.getMap().getStorage().print());
                ans.append(player.getMap().getWell().print());
                for (Workshop workshop : player.getMap().getWorkshops())
                    ans.append(workshop.print());
                ans.append(helicopter.print());
                ans.append(truck.print());
                break;
            case "map":
                ans = new StringBuilder(player.getMap().print());
                break;
            case "well":
                ans = new StringBuilder(player.getMap().getWell().print());
                break;
            case "workshop":
                for (Workshop workshop : player.getMap().getWorkshops())
                    ans.append(workshop.print());
                break;
            case "warehouse":
                ans = new StringBuilder(player.getMap().getStorage().print());
                break;
            case "truck":
                ans = new StringBuilder(truck.print());
                break;
            case "helicopter":
                ans = new StringBuilder(helicopter.print());
                break;
        }
        return ans.toString();

    }

    public void add(String command) {
        String[] words = command.split("\\s+");
        String type = words[0].toLowerCase();
        Account account = player.getAccount();
        Map map = player.getMap();
        switch (type) {
            case "cat":
                try {
                    account.spend("buy", "Cat", false);
                } catch (Exception ignore) {
                    event.printStatus("Not enough money");
                    return;
                }
                if (map.cat != -1) {
                    event.printStatus("There's already a cat");
                    return;
                }
                addAnimalToMap(map, new Cat(utils.getID("animal"), 0L, 0L, Constants.CAT_SPEED));
                try {
                    account.spend("buy", "Cat", true);
                } catch (Exception ignore) {
                }
                break;
            case "dog":
                try {
                    account.spend("buy", "Dog", false);
                } catch (Exception ignore) {
                    event.printStatus("Not enough money");
                    return;
                }
//                if (map.dog != -1) {
//                    event.printStatus("There's already a dog");
//                    return;
//                }
                addAnimalToMap(map, new Dog(utils.getID("animal"), 0L, 0L, Constants.DOG_SPEED, 2));
                try {
                    account.spend("buy", "Dog", true);
                } catch (Exception ignore) {
                }
                break;
            case "turkey":
            case "chicken":
            case "cow":
            case "sheep":
                String t = Character.toString(type.charAt(0)).toUpperCase() + type.substring(1);
                int speed;
                switch (type) {
                    case "chicken":
                        speed = Constants.CHICKEN_SPEED;
                        break;
                    case "turkey":
                        speed = Constants.TURKEY_SPEED;
                        break;
                    case "cow":
                        speed = Constants.COW_SPEED;
                        break;
                    default:
                        speed = Constants.SHEEP_SPEED;
                        break;
                }
                try {
                    account.spend("buy", t, false);
                } catch (Exception ignore) {
                    event.printStatus("Not enough money");
                    return;
                }
                addAnimalToMap(map, new FarmAnimal(utils.getID("animal"), t, 0L, 0L, speed, time));
                try {
                    account.spend("buy", t, true);
                } catch (Exception ignore) {
                }
                break;
            case "workshop":
                String workshopType = words[1];
                if(player.getMap().getWorkshop(workshopType) != null) {
                    event.printStatus("Workshop already exists");
                    return;
                }
                try {
                    account.spend("buy", String.format("workshop ", workshopType), false);
                }
                catch (Exception ignore) {
                    event.printStatus("Not enough money");
                    return;
                }
                map.addWorkshop(new Workshop(utils.getID("workshop"), workshopType));
                break;
            default:
                break;
        }
    }

    public void pickup(String command) {
        String[] words = command.split("\\s+");
        long x = Long.parseLong(words[0]);
        long y = Long.parseLong(words[1]);
        player.getMap().pickupProduct(new Pair<>(x, y));
    }

    public void buy(String command) {
        String[] words = command.split("\\s+");
        String type = words[0];
        int count = Integer.parseInt(words[1]);
        Account account = player.account;
        long cost = count * account.getCost("buy", type);
        if(cost > account.getMoney()){
            event.printStatus("Not enough money");
            return;
        }
        try {
            event.printStatus(account.spend("buying " + type, cost, true));
        }
        catch (Exception ignore){}
        event.printStatus(helicopter.buy(type, count));
    }

    public void completeBuy() {
        Pair<ArrayList<Product>, String> res = helicopter.completeBuying();
        event.printStatus(res.y);
        event.printStatus(player.getMap().scatter(res.x));
    }

    public void sell(String command) {
        String[] words = command.split("\\s+");
        String type = words[0];
        int count = Integer.parseInt(words[1]);
        ArrayList<Product> items = new ArrayList<>();
        items.add(new Product(null, type, time));
        while (count -- > 0)
            event.printStatus(truck.sell(player.getMap().getStorage() ,type, items));
    }

    public void completeSell(String command) {
        event.printStatus(truck.completeSelling(player.getMap().getStorage(), player.getAccount()));
    }

    public void cage(String command) {
        String[] words = command.split("\\s+");
        int id = Integer.parseInt(words[0]);
        Map map = player.getMap();
        event.printStatus(map.cageWild(id));
    }

    public void storeWild(String command) {
        String[] words = command.split("\\s+");
        int id = Integer.parseInt(words[0]);
        Map map = player.getMap();
        event.printStatus(map.storeWild(id));
    }

    public void plant(String command){
        String[] words = command.split("\\s+");
        long x = Long.parseLong(words[0]);
        long y = Long.parseLong(words[1]);
        int count = 1;
        if(words.length > 2)
            count = Integer.parseInt(words[2]);
        player.getMap().plantGrass(new Pair<>(x, y), count);
    }

    public void well(String command){
        Account account = player.getAccount();
        Well well = player.getMap().getWell();
        long cost = well.reFillCost();
        if(account.getMoney() < cost) {
            event.printStatus("Not enough money to refill well");
            return;
        }
        try {
            event.printStatus(account.spend("refilling the well", cost, true));
        }
        catch (Exception ignore){}
        well.reFill();
        event.printStatus("refilled the well");
    }

    private void upgrade(UpgradeableObject obj, String type) {
        if(obj == null) {
            event.printStatus(String.format("%s not found", type));
            return;
        }
        Account account = player.getAccount();
        try {
            account.spend("upgrade" + Integer.toString(obj.getLevel()), type, false);
        }
        catch (Exception ignore) {
            event.printStatus("Not enough money");
            return;
        }
        String res = obj.upgrade();
        if(res.endsWith("fully upgraded")){
            event.printStatus(res);
            return;
        }
        try {
            event.printStatus(account.spend("upgrade" + Integer.toString(obj.getLevel()), type, false));
        }
        catch (Exception ignore){}
        event.printStatus(res);
    }

    public void upgrade(String command) {
        String[] words = command.split("\\s+");
        String type = words[0];
        if(type.toLowerCase().equals("cat")) {
            Cat cat = player.getMap().getCat();
            upgrade(cat, "Cat");
        }
        else if(type.toLowerCase().equals("dog")) {
            Dog dog = player.getMap().getDog();
            upgrade(dog, "Dog");
        }
        else if(type.toLowerCase().equals("helicopter"))
            upgrade(helicopter, "helicopter");
        else if(type.toLowerCase().equals("truck"))
            upgrade(truck, "truck");
        else if(type.toLowerCase().equals("well"))
            upgrade(player.getMap().getWell(), "well");
        else if(type.toLowerCase().equals("warehouse"))
            upgrade(player.getMap().getStorage(), "warehouse");
        else if(type.toLowerCase().equals("workshop")) {
            int id = Integer.parseInt(words[1]);
            Workshop workshop = player.getMap().getWorkshopsMap().get(id);
            if(workshop == null) {
                event.printStatus("No such workshop found");
            }
            else
                upgrade(workshop, String.format("workshop ", workshop.getType()));
        }
    }

    public void produce(String command) {
        Workshop workshop = player.getMap().getWorkshop(command.split("\\s+")[0]);
        if(workshop == null) {
            event.printStatus("No such workshop");
            return;
        }
        produce(workshop.getID());
    }

    public void produce(int id) {
        Workshop workshop = player.getMap().getWorkshopsMap().get(id);
        if(workshop == null) {
            event.printStatus("No such workshop");
            return;
        }
        Pair<ArrayList<Product>, String> res = workshop.produce(player.getMap().getStorage());
        event.printStatus(res.y);
        ArrayList<Product> products = res.x;
        if(products.isEmpty())  return;
        String productType = products.get(0).getType();
        if(productType.equals("Turkey")) {
            for(int i = 0; i < products.size(); ++ i)
                add("turkey");
            return;
        }
        player.getMap().scatter(products);
    }

    public void turn(String command){
        int turns = Integer.parseInt(command);
        for(int i = 0; i < turns; ++ i)
            update(time + 1);

    }

    public void update(int time) {
        this.time = time;
        StringBuilder str = new StringBuilder();
        if (time % Constants.WILD_ATTACK_TIME == 0) {
            ArrayList<Animal> allAnimals = player.getMap().getAnimals();
            int wilds = 0;
            for(Animal animal: allAnimals)
                if(animal instanceof Wild)
                    wilds ++;
            if(wilds < Constants.MAX_WILDS) {

                String type;
                if ((new Random()).nextBoolean())
                    type = "Lion";
                else
                    type = "Bear";
                int wildID = utils.getID("animal");
                Pair<Long, Long> loc = addAnimalToMap(player.getMap(), new Wild(wildID, type, 0, 0, Constants.WILD_SPEED, 1));
                event.printStatus(String.format("%s attacked (%d, %d)\n", type.toLowerCase(), loc.x, loc.y));
                Cell cell = player.getMap().getCell(loc);
                cell.clearProducts();
                boolean isDogThere = false;
                for (Animal animal : cell.getAnimals()) {
                    if (animal.getId() != wildID) {
                        str.append(String.format("%s in (%d, %d) died\n", animal.getType(), loc.x, loc.y));
                    }
                    if (animal.getType().equals("Dog"))
                        isDogThere = true;
                    if (animal.getId() != wildID)
                        animal.die();
                }
                if (isDogThere) {
                    str.append(String.format("%s in (%d, %d) died\n", type, loc.x, loc.y));
                    for (Animal animal : cell.getAnimals()) {
                        if (animal.getId() == wildID)
                            animal.die();
                    }

                }
            }

        }
        event.printStatus(str + player.update(time));
    }
}
