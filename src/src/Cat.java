import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Cat extends Animal implements UpgradeableObject {
    Pair<Long, Long> goal = null;
    public Cat(int ID, long x, long y, int speed) {
        super(ID, x, y, speed, "Cat",0);
    }

    @Override
    public JSONObject dump() {
        JSONObject object = super.dump();
        if(goal != null)
            object.put("goal", goal);
        return object;
    }

    public Cat(JSONObject object) {
        super(object);
        if(object.has("goal"))
            goal = new Pair<>(object.getJSONObject("goal"));
    }

    private void move(GameMap map) {
        setDirection(new Pair<>(0L, 0L));
        if(goal.equals(getLocation()))
            goal = null;
        Graph graph = new Graph(map);
        setDirectionTowardsGoal(graph);
        if(goal == null) {
            ArrayList<Pair<Long, Long>> items = new ArrayList<>();
            Pair<Long, Long> nearest = null;
            long mn = 1000 * 1000 * 1000L;
            graph.bfs(getLocation());
            for(Pair<Long, Long> u: graph.distances.keySet()) {
                long dis = graph.distances.get(u);
                if(u.equals(getLocation()) || map.getCell(u).getProducts().isEmpty())  continue;
                if(mn > dis) {
                    mn = dis;
                    nearest = u;
                }
                items.add(u);
            }
            if(!items.isEmpty()) {
                if (getLevel() == 1)
                    goal = items.get((new Random()).nextInt(items.size()));
                else
                    goal = nearest;
            }
        }
        setDirectionTowardsGoal(graph);
    }

    private void setDirectionTowardsGoal(Graph graph) {
        if(goal != null) {
            Pair<Long, Long> nextMove = graph.nextMoveTowards(getLocation(), goal);
            if(nextMove == null)    goal = null;
            else {
                setDirection(nextMove);
            }
        }
    }

    public ArrayList<Product> collect(GameMap map) {
        MapCell cell = map.getCell(getLocation());
        ArrayList<Product> items = cell.getProducts();
        cell.clearProducts();
        move(map);
        return items;
    }

    public String upgrade(){
        if(level >= 2)
            return "Cat is fully upgraded";
        ++ level;
        return String.format("%s%d has upgraded to level %d", type, id, level);
    }
}
