public class Wild extends Animal {

    private boolean prisoned;         //For stating that this wild animal is in the prisoned or not
    private final int size;
    private final int price;
    private int prisonedTime;

    public Wild(int ID, String type, long x, long y, int speed, int SUF) {
        super(ID, x, y, speed, type, SUF);
        prisoned = false;
        switch (this.getType()) {
            case "lion": { this.size = 20; this.price = 150; break;}
            case "bear": { this.size = 20; this.price = 100; break;}
            default: {this.size = 0; this.price = 0;}
        }
    }

    public void breakOut() { prisoned = false; }
    public boolean isPrisoned() { return prisoned; }

    public int getPrisonedTime() {
        return prisonedTime;
    }

    public String kill(FarmAnimal farmAnimal){      // Killing farm animals
        farmAnimal.die();
        return String.format("%s%d has killed %s%d", this.getType(), this.getId(), farmAnimal.getType(), farmAnimal.getId());
    }

    public String destroy(Product product){         //Destroying products on the ground
        product.destroy();
        return String.format("%s%d had destroyed %s", this.getType(), this.getId(), product.getType());
    }

    public int getSize() {
        return size;
    }

    public String prison(int time){
        prisoned = true;
        prisonedTime = time;
        this.setSpeed(0);
        return String.format("%s%d has been prisoned", getType(), getId());
    }
}
