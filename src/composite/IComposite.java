package composite;

import java.util.List;

public interface IComposite<T, U> {
    public T save(T object);

    public T get(U key);

    public boolean delete(U key);

    public T update(T object);

    public List<T> list();

    public void hydrate();
}
