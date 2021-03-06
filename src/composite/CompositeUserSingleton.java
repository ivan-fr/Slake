package composite;

import cache.CacheRepository;
import models.Server;
import models.User;
import repository.ServerRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class CompositeUserSingleton implements IComposite<User, String> {
    public final static CompositeUserSingleton compositeUserSingleton = new CompositeUserSingleton();
    private final CacheRepository<User> userCacheRepository = new CacheRepository<>();

    private CompositeUserSingleton() {

    }

    @Override
    public User save(User object) {
        User savedUser = UserRepository.userRepository.save(object);
        if (savedUser != null) {
            userCacheRepository.save(savedUser);
        }
        return savedUser;
    }

    @Override
    public User get(String key) {
        if (userCacheRepository.get(key) == null) {
            User u = UserRepository.userRepository.get(key);
            userCacheRepository.save(u);
            return u;
        }

        return userCacheRepository.get(key);
    }

    @Override
    public boolean delete(String key) {
        if (UserRepository.userRepository.delete(key)) {
            if (userCacheRepository.get(key) != null) {
                userCacheRepository.delete(userCacheRepository.get(key));
            }

            return true;
        }

        return false;
    }

    @Override
    public User update(User object) {
        User newUser = UserRepository.userRepository.update(object);
        userCacheRepository.update(object, newUser);
        return newUser;
    }

    public User addServer(User user, Server server) {
        if (user == null) {
            return null;
        }

        if (server == null) {
            return null;
        }

        User newUser = UserRepository.userRepository.addServer(user, server);
        userCacheRepository.update(user, newUser);
        CompositeServerSingleton.compositeServerSingleton.serverCacheRepository
                .update(server, ServerRepository.serverRepository.get((Integer) server.getKey()));
        return newUser;
    }

    @Override
    public List<User> list() {
        return userCacheRepository.list();
    }

    @Override
    public void hydrate() {
        ArrayList<User> users = UserRepository.userRepository.list();
        userCacheRepository.clear();

        for (User user : users) {
            userCacheRepository.save(user);
        }
    }
}
