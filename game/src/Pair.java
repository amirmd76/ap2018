import org.json.JSONObject;

import java.security.PublicKey;

public class Pair<T1, T2> extends  Object {
    public T1 x = null;
    public T2 y = null;
    public Pair() {
        x = null;
        y = null;
    }
    public Pair(T1 x, T2 y) {
        this.x = x;
        this.y = y;
    }
    public Pair(Pair<T1, T2> p) {
        this.x = p.x;
        this.y = p.y;
    }

    public JSONObject dump() {
        JSONObject obj = new JSONObject();
        obj.put("x", x);
        obj.put("y", y);
        return obj;
    }

    public Pair(JSONObject object) {
        x = (T1)object.get("x");
        y = (T2)object.get("y");
    }

    @Override
    public boolean equals(Object p) {
        if(p == null || p.getClass()!= this.getClass())
            return false;
        Pair<T1, T2> x;
        x = (Pair<T1, T2>)p;
        return this.x.equals(x.x) && this.y.equals(x.y);
    }


    @Override
    public int hashCode() {
        return x.hashCode() * 700 + y.hashCode();
    }
}
