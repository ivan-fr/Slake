package models;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractModel {
    private Object key;
    private final HashMap<String, Object> manyToOneReferences = new HashMap<>();
    private final HashMap<String, ArrayList<Object>> oneToManyReferences = new HashMap<>();
    private final HashMap<String, ArrayList<Object>> manyToManyReferences = new HashMap<>();

    public HashMap<String, Object> getManyToOneReferences() {
        return manyToOneReferences;
    }

    public HashMap<String, ArrayList<Object>> getOneToManyReferences() {
        return oneToManyReferences;
    }

    public HashMap<String, ArrayList<Object>> getManyToManyReferences() {
        return manyToManyReferences;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String toStringWithoutRelation() {
        return "";
    }
}
