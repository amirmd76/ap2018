import java.util.ArrayList;

public class Controller {
    Player player;
    Transportation truck = new Transportation("truck"),
            helicopter = new Transportation("helicopter");
    Event event;
    ControllerUtils utils = new ControllerUtils();
    int time = 0;

    Controller(Event event) {
        this.event = event;
    }

    public void addAnimalToMap(Map map, Animal animal) {
        Pair<Long, Long> loc = map.getRandomCell();
        map.getCell(loc).addAnimal(animal);
        animal.setLocation(loc);
        if(animal.getType().equals("Cat"))
            map.cat = animal.getId();
        else if(animal.getType().equals("Dog"))
            map.dog = animal.getId();
        event.printStatus(String.format("A new %s added to (%d, %d)", animal.getType().toLowerCase(), loc.x, loc.y));
    }

    public void add(String command) {
        String[] words = command.split(" ");
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
                if (map.dog != -1) {
                    event.printStatus("There's already a dog");
                    return;
                }
                addAnimalToMap(map, new Dog(utils.getID("animal"), 0L, 0L, Constants.DOG_SPEED, 2));
                try {
                    account.spend("buy", "Dog", true);
                } catch (Exception ignore) {
                }
                break;
            case "turkey":
            case "cow":
            case "sheep":
                String t = Character.toString(type.charAt(0)).toUpperCase() + type.substring(1);
                int speed;
                switch (type) {
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
                addAnimalToMap(map, new FarmAnimal(utils.getID("animal"), t, 0L, 0L, speed));
                try {
                    account.spend("buy", t, true);
                } catch (Exception ignore) {
                }
                break;
            case "workshop":
                String workshopType = words[1];
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

    public void buy(String command) {
        String[] words = command.split(" ");
        String type = words[0];
        int count = Integer.parseInt(words[1]);
        event.printStatus(helicopter.buy(type, count));
    }

    public void sell(String command) {

    }

    public void completeBuy() {
        Pair<ArrayList<Product>, String> res = helicopter.completeBuying();
        event.printStatus(res.y);
        event.printStatus(player.getMap().scatter(res.x));
    }

    public void cage(String command) {
        String[] words = command.split(" ");
        int id = Integer.parseInt(words[0]);
        Map map = player.getMap();
        event.printStatus(map.cageWild(id));
    }

    public void storeWild(String command) {
        String[] words = command.split(" ");
        int id = Integer.parseInt(words[0]);
        Map map = player.getMap();
        event.printStatus(map.storeWild(id));
    }

    public void plant(String command){
        String[] words = command.split(" ");
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
        String[] words = command.split(" ");
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

    public void turn(String command){
        update(time + 1);

    }
    public void start(String command) {

    }
    public void update(int time) {
        this.time = time;
        player.update(time);
    }
}
