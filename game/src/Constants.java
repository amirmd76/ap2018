import java.util.ArrayList;
import java.util.HashMap;

public class Constants {
    public final static long MAX_TIME_WITHOUT_GRASS_FOR_FARM_ANIMALS = 40;
    public final static int EAT_TIME = 20;
    final static long MAX_TIME_WITHOUT_GRASS_TO_PRODUCE = 10;
    final static int MAX_WILDS = 3;

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

    final static long Initial_Player_Money = 1000;
    final static int CAT_SPEED = 38;
    final static int DOG_SPEED = 65;
    final static int COW_SPEED = 40;
    final static int TURKEY_SPEED = 50;
    final static int SHEEP_SPEED = 55;
    final static int WILD_SPEED = 45;
    final static int CHICKEN_SPEED = 40;

    final static double EGG_DEPOT_SIZE = 1;
    final static double EGGPOWDER_DEPOT_SIZE = 2;
    final static double WOOL_DEPOT_SIZE = 5;
    final static double MILK_DEPOT_SIZE = 10;
    final static double FLOUR_DEPOT_SIZE = 1.5;
    final static double THREAD_DEPOT_SIZE = 3;
    final static double COOKIE_DEPOT_SIZE = 3;
    final static double FABRIC_DEPOT_SIZE = 6;
    final static double CLOTHE_DEPOT_SIZE = 12;
    final static double CAKE_DEPOT_SIZE = 5;
    final static double DECORATION_DEPOT_SIZE = 10;
    final static double LION_DEPOT_SIZE = 20;
    final static double BEAR_DEPOT_SIZE = 25;

    final static long EGG_SALE_COST = 10;
    final static long EGGPOWDER_SALE_COST = 25;
    final static long WOOL_SALE_COST = 100;
    final static long MILK_SALE_COST = 1000;
    final static long FLOUR_SALE_COST = 10;
    final static long THREAD_SALE_COST = 150;
    final static long COOKIE_SALE_COST = 75;
    final static long FABRIC_SALE_COST = 300;
    final static long CLOTHE_SALE_COST = 400;
    final static long CAKE_SALE_COST = 100;
    final static long DECORATION_SALE_COST = 100;
    final static long LION_SALE_COST = 150;
    final static long BEAR_SALE_COST = 200;

    final static long EGG_BUY_COST = 20;
    final static long EGGPOWDER_BUY_COST = 50;
    final static long WOOL_BUY_COST = 200;
    final static long MILK_BUY_COST = 2000;
    final static long FLOUR_BUY_COST = 20;
    final static long THREAD_BUY_COST = 300;
    final static long COOKIE_BUY_COST = 150;
    final static long FABRIC_BUY_COST = 400;
    final static long CLOTHE_BUY_COST = 600;
    final static long CAKE_BUY_COST = 200;
    final static long DECORATION_BUY_COST = 150;
    final static long LION_BUY_COST = 150;
    final static long BEAR_BUY_COST = 200;


    final static int PRODUCT_DYING_TIME = 20;
    final static int WILD_ATTACK_TIME = 30;

    final static HashMap<String, String[]> WORKSHOP_INPUT_TYPES = new HashMap<String, String[]>(){{put("eggPowderPlant", new String[]{"egg"}); put("cookieBakery", new String[]{"eggPowder"});
        put("cakeBakery", new String[]{"flour", "cookie"}); put("spinnery", new String[]{"wool"}); put("weavingFactory", new String[]{"thread"}); put("sewingFactory", new String[]{"decoration", "fabric"});}};

    final static HashMap<String, Long[]> WORKSHOP_INPUT_SIZES = new HashMap<String, Long[]>();

    final static HashMap<String, String> WORKSHOP_RETURN_TYPES = new HashMap<String, String>(){{put("eggPowderPlant", "eggPowder"); put("cookieBakery", "cookie"); put("cakeBakery", "cake");
        put("spinnery", "thread"); put("weavingFactory", "fabric"); put("sewingFactory", "clothe");}};

    final static HashMap<String, Long> LEVEL1 = new HashMap<String, Long>(){{put("egg", 10L); put("turkey", 2L);}};
    final static HashMap<String, Long> LEVEL2 = new HashMap<String, Long>(){{put("egg", 20L); put("turkey", 5L); put("money", 1500L);}};
    final static HashMap<String, Long> LEVEL3 = new HashMap<String, Long>(){{put("eggPowder", 10L); put("egg", 25L);}};
    final static HashMap<String, Long> LEVEL4 = new HashMap<String, Long>(){{put("eggPowder", 10L); put("turkey", 5L); put("money", 2000L);}};
    final static HashMap<String, Long> LEVEL5 = new HashMap<String, Long>(){{put("flour", 5L); put("eggPowder", 15L);}};
    final static HashMap<String, Long> LEVEL6 = new HashMap<String, Long>(){{put("cookie", 10L); put("turkey", 20L);}};
    final static HashMap<String, Long> LEVEL7 = new HashMap<String, Long>(){{put("cookie", 10L); put("money", 2500L);}};
    final static HashMap<String, Long> LEVEL8 = new HashMap<String, Long>(){{put("cookie", 20L); put("flour", 30L);}};
    final static HashMap<String, Long> LEVEL9 = new HashMap<String, Long>(){{put("cookie", 15L); put("cake", 5L);}};
    final static HashMap<String, Long> LEVEL10 = new HashMap<String, Long>(){{put("cookie", 20L); put("cake", 10L); put("money", 3500L);}};
    final static HashMap<String, Long> LEVEL11 = new HashMap<String, Long>(){{put("money", 2000L); put("cake", 10L); put("cow", 2L);}};
    final static HashMap<String, Long> LEVEL12 = new HashMap<String, Long>(){{put("milk", 10L); put("cow", 5L); put("money", 3500L);}};
    final static HashMap<String, Long> LEVEL13 = new HashMap<String, Long>(){{put("sheep", 2L); put("milk", 10L); put("cow", 2L);}};
    final static HashMap<String, Long> LEVEL14 = new HashMap<String, Long>(){{put("wool", 10L); put("sheep", 5L); put("money", 2000L);}};
    final static HashMap<String, Long> LEVEL15 = new HashMap<String, Long>(){{put("cotton", 10L); put("wool", 10L); put("sheep", 10L);}};

    final static ArrayList<String> LEVEL1_WORKSHOPS = new ArrayList<String>(){{}};
    final static ArrayList<String> LEVEL2_WORKSHOPS = new ArrayList<String>(){};
    final static ArrayList<String> LEVEL3_WORKSHOPS = new ArrayList<String>(){{add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL4_WORKSHOPS = new ArrayList<String>(){{add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL5_WORKSHOPS = new ArrayList<String>(){{add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL6_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL7_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL8_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant");}};
    final static ArrayList<String> LEVEL9_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery");}};
    final static ArrayList<String> LEVEL10_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery");}};
    final static ArrayList<String> LEVEL11_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery");}};
    final static ArrayList<String> LEVEL12_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery");}};
    final static ArrayList<String> LEVEL13_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery"); add("spinnery");}};
    final static ArrayList<String> LEVEL14_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery"); add("spinnery");}};
    final static ArrayList<String> LEVEL15_WORKSHOPS = new ArrayList<String>(){{add("cookieBakery"); add("eggPowderPlant"); add("cakeBakery"); add("spinnery"); add("weavingFactory");}};
}
