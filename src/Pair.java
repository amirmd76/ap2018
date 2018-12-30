import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;

public class Pair<T1, T2> {
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
        try {
            obj.put("x", x);
            obj.put("y", y);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Pair(JSONObject object) {
        try {
            x = (T1)object.get("x");
            y = (T2)object.get("y");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Pair<T1, T2> p) {
        return x.equals(p.x) && y.equals(p.y);
    }

    @Override
    public int hashCode() {
        return x.hashCode() * 700 + y.hashCode();
    }
}
