import java.util.ArrayList;

public class Cell implements MapCell {
    private ArrayList<Product> products;
    private ArrayList<Animal> animals;
    private int grass;
    public Cell() {
        products = new ArrayList<>();
        animals = new ArrayList<>();
        grass = 0;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean isBlocked() { // TODO: IMPLEMENT
        return false;
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void clearProducts() {
        products.clear();
    }

    public void clearAnimals() { animals.clear(); }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public int getGrass() {
        return grass;
    }

    public void setGrass(int grass) {
        this.grass = grass;
    }

    void update() {
        ArrayList<Animal> res = new ArrayList<>();
        for(Animal animal: animals)
            if(animal.isAlive())
                res.add(animal);
        this.animals = new ArrayList<>(res);
    }

    Animal removeAnimal(int id) {
        int idx = -1;
        for(int i = 0; i < animals.size(); ++ i)
            if(animals.get(i).getId() == id)
                idx = i;
        Animal res = null;
        if(idx != -1) {
            res = animals.get(idx);
            animals.remove(idx);
        }
        return res;
    }

    Product removeProduct(String type) {
        int idx = -1;
        for(int i = 0; i < products.size(); ++ i)
            if(products.get(i).getType().equals(type)) {
                idx = i;
                break;
            }
        if(idx == -1)
            return null;
        Product res = products.get(idx);
        products.remove(idx);
        return res;
    }

    Product getProduct(String type) {
        int idx = -1;
        for(int i = 0; i < products.size(); ++ i)
            if(products.get(i).getType().equals(type)) {
                idx = i;
                break;
            }
        if(idx == -1)
            return null;
        Product res = products.get(idx);
        return res;
    }
}
