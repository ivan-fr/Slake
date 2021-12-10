package repository;

import models.Server;

import java.util.ArrayList;

public class ServerRepository implements IRepository<Server, Integer> {
    @Override
    public Server save(Server object) {
        return null;
    }

    @Override
    public Server get(Integer key) {
        return null;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public Server update(Server object) {
        return null;
    }

    @Override
    public ArrayList<Server> list() {
        return null;
    }
}
