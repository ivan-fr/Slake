package Cache;

import models.AbstractModel;
import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheRepository<T> {

    private HashMap<Object, T> map = new HashMap<>();

    public HashMap<Object, T> getMap() {
        return map;
    }

    public T save(AbstractModel instance) {
        map.put(instance.getKey(), (T) instance);
        return (T) instance;
    }

    public T get(Object key) {
        return (T) map.get(key);
    }

    public boolean delete(AbstractModel instance) {
        return map.remove(instance.getKey(), (T) instance);
    }

    public void update(AbstractModel oldInstance, AbstractModel newInstance) {
        map.replace(newInstance.getKey(), (T) oldInstance, (T) newInstance);
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
