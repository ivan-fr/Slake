package composite;

import cache.CacheRepository;
import models.Server;
import repository.ServerRepository;

import java.util.ArrayList;
import java.util.List;

public class CompositeServerSingleton implements IComposite<Server, Integer> {
    public final static CompositeServerSingleton compositeServerSingleton = new CompositeServerSingleton();
    public final CacheRepository<Server> serverCacheRepository = new CacheRepository<>();

    private CompositeServerSingleton() {

    }

    @Override
    public Server save(Server object) {
        Server savedServer = ServerRepository.serverRepository.save(object);
        if (savedServer != null) {
            serverCacheRepository.save(savedServer);
        }
        return savedServer;
    }

    @Override
    public Server get(Integer key) {
        if (serverCacheRepository.get(key) == null) {
            Server u = ServerRepository.serverRepository.get(key);
            System.out.println(u);
            u = serverCacheRepository.save(u);
            System.out.println(u);
            return u;
        }

        return serverCacheRepository.get(key);
    }

    @Override
    public boolean delete(Integer key) {
        if (ServerRepository.serverRepository.delete(key)) {
            if (serverCacheRepository.get(key) != null) {
                serverCacheRepository.delete(serverCacheRepository.get(key));
            }

            return true;
        }

        return false;
    }

    @Override
    public Server update(Server object) {
        Server newServer = ServerRepository.serverRepository.update(object);
        serverCacheRepository.update(object, newServer);
        return newServer;
    }

    @Override
    public List<Server> list() {
        return serverCacheRepository.list();
    }

    @Override
    public void hydrate() {
        ArrayList<Server> servers = ServerRepository.serverRepository.list();
        serverCacheRepository.clear();

        for (Server server : servers) {
            serverCacheRepository.save(server);
        }
    }
}
