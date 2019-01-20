import java.util.HashMap;

public class Constants {
    public final static long MAX_TIME_WITHOUT_GRASS_FOR_FARM_ANIMALS = 40;
    public final static int EAT_TIME = 2;
    final static long MAX_TIME_WITHOUT_GRASS_TO_PRODUCE = 10;

    final static long WAREHOUSE_INITIAL_CAPACITY = 50;
    final static long[] WAREHOUSE_UPGRADE_CAPACITY = {50, 150, 300, 600};
    final static String DUMP_FILE = "farm_frenzy_ap.json";

    final static long[] Well_Water_Capacity = {5, 7, 10, 100};
    final static int Well_Max_Level_Upgrade = 4;
    final static int[] WELL_REFILL_COST = {19, 17, 15, 7};

    final static long Truck_Initial_Box_Capacity = 2;      //TODO Handling all constants
    final static int Truck_Initial_Travel_Duration = 20;
    final static int Truck_Upgrade_Travel_Duration = 5;
    final static int Truck_Max_Level_Upgrade = 4;


    final static int MAP_HEIGHT = 9;
    final static int MAP_WIDTH = 11;
    final static long Helicopter_Initial_Box_Capacity = 2;
    final static int Helicopter_Initial_Travel_Duration = 12;
    final static int Helicopter_Upgrade_Travel_Duration = 3;
    final static int Helicopter_Max_Level_Upgrade = 4;
    final static int WILD_CAGE_TIME = 10;

    final static long Initial_Player_Money = 10000;
    final static int CAT_SPEED = 38;
    final static int DOG_SPEED = 65;
    final static int COW_SPEED = 40;
    final static int TURKEY_SPEED = 50;
    final static int CHICKEN_SPEED = 50;
    final static int SHEEP_SPEED = 55;
    final static int WILD_SPEED = 45;


    final static int PRODUCT_DYING_TIME = 20;
    final static int WILD_ATTACK_TIME = 30;

    final static HashMap<String, String[]> WORKSHOP_INPUT_TYPES = new HashMap<>();
    final static HashMap<String, Long[]> WORKSHOP_INPUT_SIZES = new HashMap<>();
    final static HashMap<String, String> WORKSHOP_RETURN_TYPES = new HashMap<>();
}
