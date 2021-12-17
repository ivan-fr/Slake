package bdd;

import models.Channel;
import models.Message;
import models.Server;
import models.User;
import repository.ChannelRepository;
import repository.MessageRepository;
import repository.ServerRepository;
import repository.UserRepository;

import java.util.Date;

public class Seed {
    private static final UserRepository userRepository = UserRepository.userRepository;
    private static final ServerRepository serverRepository = ServerRepository.serverRepository;

    public static void main(String[] args) {
        try {
            User u = new User("user1");
            userRepository.save(u);

            User u1 = new User("user2");
            userRepository.save(u);

            User u2 = new User("user3");
            userRepository.save(u);

            User u3 = new User("user4");
            userRepository.save(u);

            User u4 = new User("user5");
            userRepository.save(u);

            Server s = new Server("server1");
            serverRepository.save(s);

            userRepository.addServer(u, s);
            userRepository.addServer(u1, s);
            userRepository.addServer(u2, s);

            Server s1 = new Server("server2");
            serverRepository.save(s1);

            userRepository.addServer(u3, s1);
            u4 = userRepository.addServer(u4, s1);

            Channel c = new Channel("chan1", (Integer) s.getKey());
            ChannelRepository.channelRepository.save(c);

            Channel c2 = new Channel("chan2", (Integer) s1.getKey());
            ChannelRepository.channelRepository.save(c2);

            Message m = new Message("message1", new Date(), (String) u4.getKey(), (Integer) c2.getKey());
            MessageRepository.messageRepository.save(m);
            System.out.println("Seed success");
        } catch (Exception e) {
            System.out.println("Seed fail");
        }


    }
}
