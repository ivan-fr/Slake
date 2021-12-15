package bdd;

import models.User;
import repository.UserRepository;

public class Seed {
    private static final UserRepository userRepository = UserRepository.userRepository;

    public static void main(String[] args) {
        User u = new User("user1");
        userRepository.save(u);

        u = new User("user2");
        userRepository.save(u);

        u = new User("user3");
        userRepository.save(u);

        u = new User("user4");
        userRepository.save(u);

        u = new User("user5");
        userRepository.save(u);
    }
}
