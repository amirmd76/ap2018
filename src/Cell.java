import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public String print(int x, int y) {
        StringBuilder ans = new StringBuilder(String.format("X cell (%d, %d) contains %d units of grass in it, %d products and %d animals: ", x, y,
                grass, products.size(), animals.size()));
        for(Animal animal: animals)
            ans.append(String.format("%s (id = %d), ", animal.getType(), animal.getId()));
        for(Product product: products)
            ans.append(String.format("product of type %s, ", product.getType()));
        return ans.substring(0, ans.length()-2) + "\n";
    }

    public JSONObject dump() {
        JSONObject object = new JSONObject();
        JSONArray products = new JSONArray();
        JSONArray animals = new JSONArray();
        for(Product product: this.products)
            products.put(product.dump());
        for(Animal animal: this.animals)
            products.put(animal.dump());
        try {
            object.put("products", products);
            object.put("animals", animals);
            object.put("grass", grass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public Cell(JSONObject object) {
        products = new ArrayList<>();
        animals = new ArrayList<>();

        try {
            grass = object.getInt("grass");
            JSONArray animals = object.getJSONArray("animals");
            for(int i = 0; i < animals.length(); i++) {
                JSONObject an = (JSONObject)animals.get(i);
                String type = an.getString("type");
                Animal item;
                if(type.equals("Cat"))
                    item = new Cat((JSONObject) animals.get(i));
                else if(type.equals("Dog"))
                    item = new Dog((JSONObject) animals.get(i));
                else if(type.equals("Lion") || type.equals("Bear"))
                    item = new Wild((JSONObject) animals.get(i));
                else
                    item = new FarmAnimal((JSONObject) animals.get(i));
                this.animals.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(Object product: products)
            this.products.add(new Product((JSONObject)product));
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

    public ArrayList<Product> getProducts_by_Type(String type) {
        ArrayList<Product> items = new ArrayList<>();
        for(int i = 0; i < products.size(); ++ i)
            if(products.get(i).getType().equals(type)) {
                items.add(products.get(i));
            }
        if(items.size() == 0)
            return null;
        return items;
    }

    public ArrayList<Animal> getAnimals_by_Type (String type){
        ArrayList<Animal> animals = getAnimals();
        for (int i = animals.size()-1; i >= 0; i--){
            if (!animals.get(i).getType().equals(type))
                animals.remove(animals.get(i));
        }
        return animals;
    }
}
