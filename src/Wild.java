public class Wild extends Animal {

    private boolean prisoned;         //For stating that this wild animal is in the prisoned or not

    public Wild(int ID, String type, long x, long y, int speed, int SUF) {
        super(ID, x, y, speed, type, SUF);
        prisoned = false;
    }

    public boolean isPrisoned() { return prisoned; }

    public String kill(FarmAnimal farmAnimal){      // Killing farm animals
        farmAnimal.die();
        return String.format("%s%d has killed %s%d", this.getType(), this.getId(), farmAnimal.getType(), farmAnimal.getId());
    }

    public String destroy(Product product){         //Destroying products on the ground
        product.destroy();
        return String.format("%s%d had destroyed %s", this.getType(), this.getId(), product.getType());
    }

    public String prison(){
        prisoned = true;
        this.setSpeed(0);
        return String.format("%s%d has been prisoned", getType(), getId());
    }
}
