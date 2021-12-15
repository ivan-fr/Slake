package models;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractModel {
    private Object key;
    private final HashMap<String, Integer> manyToOneReferences = new HashMap<>();
    private final HashMap<String, ArrayList<Integer>> manyToManyReferences = new HashMap<>();

    public HashMap<String, Integer> getManyToOneReferences() {
        return manyToOneReferences;
    }

    public HashMap<String, ArrayList<Integer>> getManyToManyReferences() {
        return manyToManyReferences;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }
}
