import java.util.ArrayList;
import java.util.Random;

public class Cat extends Animal {
    Pair<Long, Long> goal = null;
    public Cat(String type, long x, long y) {
        super(type, x, y);
    }

    private void move(GameMap map) {
        setDirection(new Pair<>(0L, 0L));
        if(goal.equals(location))
            goal = null;
        Graph graph = new Graph(map);
        setDirectionTowardsGoal(graph);
        if(goal == null) {
            ArrayList<Pair<Long, Long>> items = new ArrayList<>();
            Pair<Long, Long> nearest = null;
            long mn = 1000 * 1000 * 1000L;
            graph.bfs(location);
            for(Pair<Long, Long> u: graph.distances.keySet()) {
                long dis = graph.distances.get(u);
                if(u.equals(location) || map.getCell(u).getItems().isEmpty())  continue;
                if(mn > dis) {
                    mn = dis;
                    nearest = u;
                }
                items.add(u);
            }
            if(!items.isEmpty()) {
                if (level == 1)
                    goal = items.get((new Random()).nextInt(items.size()));
                else
                    goal = nearest;
            }
        }
        setDirectionTowardsGoal(graph);
    }

    private void setDirectionTowardsGoal(Graph graph) {
        if(goal != null) {
            Pair<Long, Long> nextMove = graph.nextMoveTowards(location, goal);
            if(nextMove == null)    goal = null;
            else {
                setDirection(nextMove);
            }
        }
    }

    public ArrayList<String> collect(GameMap map) {
        MapCell cell = map.getCell(location);
        ArrayList<String> items = cell.getItems();
        cell.clearItems(); // TODO: make sure this statement affects the map
        move(map);
        return items;
    }

    public void upgrade() {
        level = 2;
    }
}
