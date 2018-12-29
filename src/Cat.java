import java.util.ArrayList;
import java.util.Random;

public class Cat extends Animal {
    Pair<Long, Long> goal = null;
    public Cat(int ID, String type, long x, long y, int speed) {
        super(ID, x, y, speed, type,0);
    }

    private void move(GameMap map) {
        setDirection(new Pair<Long, Long>(0L, 0L));
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
        cell.clearProducts(); // TODO: make sure this statement affects the map
        move(map);
        return items;
    }
}
