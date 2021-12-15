package composite;

import cache.CacheRepository;
import models.AbstractModel;
import models.User;
import repository.UserRepository;

import java.util.ArrayList;

public class CompositeUserRepository {

    public final static CompositeUserRepository compositeUserRepository = new CompositeUserRepository();
    public static UserRepository userRepository ;
    public CacheRepository<User> userCacheRepository = new CacheRepository<User>();


    private CompositeUserRepository() {
    }

    public void importCache() {
        // fill the cache

    }


    public User save(User user) {
        User savedUser = userRepository.userRepository.save(user) ;
        userCacheRepository.save(user) ;
        return savedUser ;
    }


    public User get() {
        // cache

        return null ;
    }


    public boolean delete(String key) {
        // BDD

        //Cache
        return false ;
    }

    public User update(User user) {

        // BDD

        //Cache

        return null ;
    }

    public ArrayList<User> list() {
        //Cache

    }

}
