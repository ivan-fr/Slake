package cache;

import models.AbstractModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheRepository<T extends AbstractModel> {

    private final HashMap<Object, T> map = new HashMap<>();

    public HashMap<Object, T> getMap() {
        return map;
    }

    public T save(T instance) {
        map.put(instance.getKey(), instance);
        return instance;
    }

    public T get(Object key) {
        return map.get(key);
    }

    public boolean delete(T instance) {
        return map.remove(instance.getKey(), instance);
    }

    public void update(T oldInstance, T newInstance) {
        map.replace(newInstance.getKey(), oldInstance, newInstance);
    }

    public List<T> list() {
        List<T> list = new ArrayList<>();
        for (Map.Entry<Object, T> entry : map.entrySet()) {
            T value = entry.getValue();
            list.add(value);
        }
        return list;
    }

    public void clear() {
        this.map.clear();
    }
}
