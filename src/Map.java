import java.util.ArrayList;

public class Map implements GameMap {
    private long h, w;
    private Cell[][] cells;
    private ArrayList<Animal> cagedAnimals = new ArrayList<>();
    Storage storage = new Storage();

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
                cell.removeAnimal(animal.getId());
                break;
            }
        cagedAnimals.add(wild);
        return wild.prison();
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

    public String pickupProduct(Pair<Long, Long> location, String type) {
        Cell cell = getCell(location);
        Product product = cell.removeProduct(type);
        if(product == null)
            return "No such product";
        if(!storage.canStore(product.type, 1)) {
            cell.addProduct(product);
            return "Storage is full";
        }
        return storage.store(product.type, 1);
    }

    public String produce(String Workshoptype) {
        return "OK"; // TODO: implement
    }
}
