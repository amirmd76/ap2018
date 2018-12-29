public class FarmAnimal extends Animal {

    long lastTimeAte = -1;

    public FarmAnimal(int ID, String type, long x, long y, int speed) { super(ID, x, y , speed, type,0); }

    private void checkStatus(long time) {
        if(!isAlive())  return;
        if(time - lastTimeAte > Constants.MAX_TIME_WITHOUT_GRASS_FOR_FARM_ANIMALS)
            die();
    }

    public void eat(long time) {
        if(!isAlive())  return;
        // TODO: check if there's grass in current cell and eat it
        lastTimeAte = time;
        checkStatus(time);
    }

    public String create(long time) {
        if(time - lastTimeAte > Constants.MAX_TIME_WITHOUT_GRASS_TO_PRODUCE)
            return null;
        switch (getType()) {
            case "Turkey":
                return "egg";
            case "Cow":
                return "milk";
            case "Sheep":
                return "wool";
            default:
                return "INVALID TYPE";
        }
    }
}
