package repository;

import java.util.ArrayList;

public interface IRepository<T, U> {
    public T save(T object);
    public T get(U key);
    public boolean delete(String key);
    public T update(T object);
    public ArrayList<T> list();
}
