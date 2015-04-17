package info.statstrats.banindex.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

public final class Champion {

    private static final Map<Integer, Champion> byId = new HashMap<>();

    private int id;
    private String name;

    private Champion(int id, String name) {
        this.id = id;
        this.name = checkNotNull(name);
    }

    public synchronized static Champion forValues(int id, String name) {
        Champion c = byId.get(id);
        if (c == null) {
            c = new Champion(id, name);
            byId.put(id,  c);
        }
        return c;
    }

    @Override
    public String toString() {
        return name;
    }
}
