public class FarmAnimal extends Animal {
    long lastTimeAte = -1;
    public FarmAnimal(String type, long x, long y) {
        super(type, x, y);
    }

    private void checkStatus(long time) {
        if(!isAlive())  return;
        if(time - lastTimeAte > Consts.MAX_TIME_WITHOUT_GRASS_FOR_FARM_ANIMALS)
            alive = false;
    }

    public void eat(long time) {
        if(!isAlive())  return;
        // TODO: check if there's grass in current cell and eat it
        lastTimeAte = time;
        checkStatus(time);
    }

    public String create(long time) {
        if(time - lastTimeAte > Consts.MAX_TIME_WITHOUT_GRASS_TO_PRODUCE)
            return null;
        switch (type) {
            case "chicken":
                return "egg";
            case "cow":
                return "milk";
            case "sheep":
                return "wool";
            default:
                return "INVALID TYPE";
        }
    }
}
