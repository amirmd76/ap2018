import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Map implements GameMap {
    private long h, w;
    private Cell[][] cells;
    private Storage storage = new Storage();
    public int time = -1;
    int cat = -1, dog = -1;
    private Well well = new Well();
    ArrayList<Workshop> workshops = new ArrayList<>();

    public String print() {
        StringBuilder ans = new StringBuilder(String.format("Map is %d X %d", h, w));
        ans.append("\nThe cells are:\n");
        for(int i = 0; i < h; ++ i)
            for(int j = 0; j < w; ++ j)
                ans.append(cells[i][j].print(i, j));
        return ans.toString();
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        object.put("h", h);
        object.put("w", w);
        object.put("storage", storage.dump());
        object.put("time", time);
        object.put("cat", cat);
        object.put("dog", dog);
        object.put("well", well.dump());
        JSONArray array = new JSONArray();
        for(Workshop workshop: workshops)
            array.put(workshop.dump());
        object.put("workshops", array);
        array = new JSONArray();
        for(int i = 0; i < h; ++ i) {
            JSONArray row = new JSONArray();
            for(int j = 0; j < w; ++ j)
                row.put(cells[i][j].dump());
            array.put(row);
        }
        object.put("cells", array);
        return object;
    }

    public Map(JSONObject object) {
        h = object.getInt("h");
        w = object.getInt("w");
        storage = new Storage(object.getJSONObject("storage"));
        time = object.getInt("time");
        cat = object.getInt("cat");
        dog = object.getInt("dog");
        well = new Well(object.getJSONObject("well"));
        JSONArray array = object.getJSONArray("workshops");
        for(Object item: array)
            workshops.add(new Workshop((JSONObject)item));
        array = object.getJSONArray("cells");
        cells = new Cell[(int)h][];
        for(int i = 0; i < h; ++ i) {
            JSONArray row = array.getJSONArray(i);
            cells[i] = new Cell[(int)w];
            for(int j = 0; j < w; ++ j)
                cells[i][j] = new Cell(row.getJSONObject(j));
        }
    }

    public Well getWell() {
        return well;
    }

    public Storage getStorage() {
        return storage;
    }

    public void addWorkshop(Workshop workshop) {
        workshops.add(workshop);
    }

    public ArrayList<Workshop> getWorkshops() {
        return workshops;
    }

    public HashMap<Integer, Workshop> getWorkshopsMap() {
        HashMap<Integer, Workshop> workshops = new HashMap<>();
        for(Workshop workshop: this.workshops)
            workshops.put(workshop.getID(), workshop);
        return workshops;
    }


    public Map(long h, long w) {
        this.h = h;
        this.w = w;
        cells = new Cell[(int)h][];
        for(int i = 0; i < h; ++ i) {
            cells[i] = new Cell[(int)w];
            for(int j = 0; j < w; ++ j)
                cells[i][j] = new Cell();
        }
    }

    public Pair<Long, Long> getDimensions() {
        return new Pair<>(h, w);
    }
    public long getHeight() {
        return h;
    }
    public long getWidth() {
        return w;
    }
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
    public Cell getCell(long x, long y) {
        return getCell((int)x, (int)y);
    }
    public Cell getCell(Pair<Long, Long> location) {
        return getCell(location.x, location.y);
    }
    public ArrayList<Animal> getAnimals() {
        ArrayList<Animal> animals = new ArrayList<>();
        for(int i = 0; i < h; ++ i)
            for(int j = 0; j < w; ++ j)
                animals.addAll(cells[i][j].getAnimals());
        return animals;
    }

    public HashMap<Integer, Animal> getAnimalsMap() {
        HashMap<Integer, Animal> animals = new HashMap<>();
        for(int i = 0; i < h; ++ i)
            for(int j = 0; j < w; ++ j)
                for(Animal animal: cells[i][j].getAnimals())
                    animals.put(animal.getId(), animal);
        return animals;
    }

    public ArrayList<Long> getGrass() {
        ArrayList<Long> res = new ArrayList<>();
        for(int i = 0; i < h; ++ i)
            for(int j = 0; j < w; ++ j)
                if(cells[i][j].getGrass() != 0)
                    res.add((long)cells[i][j].getGrass());
        return res;
    }

    public String cageWild(Pair<Long, Long> location) {
        Cell cell = (Cell)this.getCell(location);
        Wild wild = null;
        for(Animal animal: cell.getAnimals())
            if(animal instanceof Wild) {
                wild = (Wild)animal;
                wild.prison(time);
                return String.format("caged %s-%d", wild.getType(), wild.getId());
            }
        return "No wild to cage";
    }

    public String cageWild(int id) {
        Wild wild = null;
        try {
            wild = (Wild) getAnimalsMap().get(id);
        }
        catch (Exception ignore) {
            return "No wild to cage";
        }
        wild.prison(time);

        return String.format("caged %s-%d", wild.getType(), wild.getId());
    }

    public String storeWild(Pair<Long, Long> location) {
        Cell cell = (Cell)this.getCell(location);
        Wild wild = null;
        for(Animal animal: cell.getAnimals())
            if(animal instanceof Wild) {
                wild = (Wild)animal;
                if(wild.isPrisoned()) {
                    if(!storage.canStore(wild.getType(), 1, wild.getSize()))
                        return "Storage full";
                    storage.store(wild.getType(), 1, wild.getSize());
                    cell.removeAnimal(wild.getId());
                    return String.format("stored wild %s-%d", wild.getType(), wild.getId());
                }
            }
        return "No wild to store";
    }

    public String storeWild(int id) {
        Wild wild = null;
        try {
            wild = (Wild) getAnimalsMap().get(id);
        }
        catch (Exception ignore) {
            return "No wild to store";
        }
        if(wild.isPrisoned()) {
            if(!storage.canStore(wild.getType(), 1, wild.getSize()))
                return "Storage full";
            storage.store(wild.getType(), 1, wild.getSize());
            Cell cell = getCell(wild.getLocation());
            cell.removeAnimal(wild.getId());
            return String.format("stored wild %s-%d", wild.getType(), wild.getId());
        }

        return "No wild to store";
    }

    public boolean isValidCell(Pair<Long, Long> location) {
        return 0 <= location.x && location.x < h && 0 <= location.y && location.y < w;
    }

    public String plantGrass(Pair<Long, Long> location, int count) {
        String res = well.useWater();
        if(!res.equals("Water is now provided for planting"))
            return res;
        long x = location.x, y = location.y;
        for(int dx = -1; dx <= 1; ++ dx)
            for(int dy = -1; dy <= 1; ++ dy) {
                Pair<Long, Long> cur = new Pair<>(x + dx, y + dy);
                if(!isValidCell(cur))
                    continue;
                plant(location, count);
            }
        return String.format("Planted grass in cell (%d, %d)", x, y);
    }

    public String feedPet(int id, long time) {
        boolean flag = false;
        for(int i = 0; i < h; ++ i)
            for(int j = 0; j < w; ++ j)
                for(Animal animal: cells[i][j].getAnimals())
                    if(animal.getId() == id) {
                        ((FarmAnimal) animal).eat(time);
                        flag =  true;
                    }
        if(flag)
            return String.format("Animal %d fed", id);
        return "No such animal found";
    }

    public String plant(Pair<Long, Long> location, int count) {
        Cell cell = getCell(location);
        cell.setGrass(cell.getGrass() + count);
        return String.format("planted %d units of grass in cell %d, %d", count, location.x, location.y);
    }

    public String plant(Pair<Long, Long> location) {
        Cell cell = getCell(location);
        cell.setGrass(cell.getGrass() + 1);
        return String.format("planted 1 units of grass in cell %d, %d", location.x, location.y);
    }

    public String pickupProduct(Pair<Long, Long> location) {
        Cell cell = getCell(location);
        StringBuilder str = new StringBuilder();
        for(Product product: cell.getProducts()) {
            String type = product.getType();
            if(!storage.canStore(product.type, 1, product.getSize()))
                continue;
            cell.removeProduct(type);
            storage.store(product.type, 1, product.getSize());
            str.append(String.format("Picked up product %s in (%d, %d)\n", product.getType(), location.x, location.y));
        }
        return str.toString();
    }

    public void addProduct(Pair<Long, Long> location, String type) {
        Cell cell = getCell(location);
        cell.addProduct(new Product(location, type, time));
    }

    public String scatter(ArrayList<Product> products) {
        StringBuilder str = new StringBuilder();
        for (Product product: products){
            String type = product.getType();
            Pair<Long, Long> location = getRandomCell();
            addProduct(location, type);
            str.append(String.format("%s has been placed on (%d, %d)\n", location.x, location.y));
        }
        return str.toString();
    }

    public Pair<Long, Long> getRandomCell() {
        Random rand = new Random();
        Long x = (long)rand.nextInt((int) h),
                y = (long)rand.nextInt((int) w);
        return new Pair<>(x, y);
    }

    public Cat getCat() {
        if(cat == -1)
            return null;
        return (Cat)getAnimalsMap().get(cat);
    }

    public Dog getDog() {
        if(dog == -1)
            return null;
        return (Dog)getAnimalsMap().get(dog);
    }


    public String produce(String Workshoptype) {
        return "OK"; // TODO: implement
    }

    public Workshop getWorkshop(String type) {
        for(Workshop workshop: workshops)
            if(workshop.getType().equals(type))
                return workshop;
        return null;
    }

    public String update(int time) {
        int prvTime = this.time;
        if (prvTime == -1)
            prvTime = time;
        StringBuilder str = new StringBuilder();
        this.time = time;
        ArrayList<Animal> animals = new ArrayList<>();
        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                for (Animal animal : cells[i][j].getAnimals()) {
                    animal.setLocation(new Pair<>((long) i, (long) j));
                    if(animal.isAlive()) {
                        if(cells[i][j].getGrass() > 0 && animal instanceof FarmAnimal && time - ((FarmAnimal) animal).lastTimeAte >=
                        Constants.EAT_TIME) {
                            cells[i][j].setGrass(cells[i][j].getGrass() - 1);
                            ((FarmAnimal) animal).eat(time);
                        }
                        if(animal instanceof FarmAnimal)
                            ((FarmAnimal) animal).checkStatus(time);
                        if(animal.isAlive())
                            animals.add(animal);
                        else {
                            str.append(String.format("%s in (%d, %d) starved to death\n", animal.getType(), i, j));
                        }
                    }
                    else {
                        if(animal.getType().equals("Cat"))
                            cat = -1;
                        else if(animal.getType().equals("Dog"))
                            dog = -1;
                    }
                }
                cells[i][j].clearAnimals();
            }
        for (Animal animal : animals) {
            if (animal.getType().equals("Cat")) {
                ArrayList<Product> products = ((Cat) animal).collect(this);
                while (products.size() > 0 && storage.canStore(products.get(0).type, 1, products.get(0).getSize())) {
                    Product product = products.get(0);
                    str.append(String.format("Cat picked up a(n) %s\n", product.getType()));
                    storage.store(product.getType(), 1, product.getSize());
                    products.remove(0);
                }
                for (Product product : products)
                    getCell(product.getLocation()).addProduct(product);
            }
            if (animal instanceof Wild && ((Wild) animal).isPrisoned() && time - ((Wild) animal).getPrisonedTime() >= Constants.WILD_CAGE_TIME) {
                str.append(String.format("%s-%d has broken free\n", animal.getType(), animal.getId()));
                continue;
            }
            if (!(animal instanceof Wild) || !((Wild) animal).isPrisoned())
                str.append(animal.walk(time - prvTime));
            getCell(animal.getLocation()).addAnimal(animal);
        }

        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                products.addAll(cells[i][j].getProducts());
                cells[i][j].clearAnimals();
            }
        for(Product product: products) {
            if (time - product.getCreationTime() < Constants.PRODUCT_DYING_TIME)
                getCell(product.getLocation()).addProduct(product);
            else
                str.append(String.format("Product %s removed from (%d, %d)\n", product.getType(), product.getLocation().x,
                        product.getLocation().y));
        }
        return str.toString();
    }

    public ArrayList<Product> getProducts_by_Type(String type){
        ArrayList<Product> items = new ArrayList<>();
        for (int i = 0; i < h ; i++){
            for (int j = 0; j < w; j++){
                items.addAll(cells[i][j].getProducts_by_Type(type));
            }
        }
        return items;
    }

    public ArrayList<Animal> getAnimals_by_Type(String type){
        ArrayList<Animal> animals = new ArrayList<>();
        for (int i = 0; i < h ; i++){
            for (int j = 0; j < w; j++){
                animals.addAll(cells[i][j].getAnimals_by_Type(type));
            }
        }
        return animals;
    }
}