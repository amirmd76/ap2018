import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
    static final long dx[] = {0, -1, 0, 1};
    static final long dy[] = {1, 0, -1, 0};

    private GameMap map;
    public HashMap<Pair<Long, Long>, Pair<Long, Long>> dads;
    public HashMap<Pair<Long, Long>, Long> distances;

    Graph(GameMap map) {
        this.map = map;
    }

    public void bfs(Pair<Long, Long> source) {
        dads = new HashMap<>();
        distances = new HashMap<>();
        LinkedList<Pair<Long, Long>> queue = new LinkedList<>();

        distances.put(source, 0L);
        queue.add(source);

        while(!queue.isEmpty()) {
            Pair<Long, Long> v = queue.poll();
            for(int i = 0; i < 4; ++ i) {
                long x = v.x + dx[i],
                     y = v.y + dy[i];
                Pair<Long, Long> u = new Pair<>(x, y);
                if(map.getCell(u) == null || map.getCell(u).isBlocked() || distances.containsKey(u))   continue;
                distances.put(u, distances.get(v) + 1);
                dads.put(u, v);
                queue.add(u);
            }
        }
    }

    Pair<Long, Long> nextMoveTowards(Pair<Long, Long> v, Pair<Long, Long> u) {
        bfs(v);
        if(!distances.containsKey(u))
            return null;
        if(v.equals(u))
            return new Pair<>(0L, 0L);
        Pair<Long, Long> cur = new Pair<>(u);
        while(dads.get(cur) != v)
            cur = dads.get(cur);
        return new Pair<>(cur.x - v.x, cur.y - v.y);
    }
}
