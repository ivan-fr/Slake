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
        User u = new User("user1");
        u = userRepository.save(u);

        User u1 = new User("user2");
        u1 = userRepository.save(u1);

        User u2 = new User("user3");
        u2 = userRepository.save(u2);

        User u3 = new User("user4");
        u3 = userRepository.save(u3);

        User u4 = new User("user5");
        u4 = userRepository.save(u4);

        Server s = new Server("server1");
        s = serverRepository.save(s);

        userRepository.addServer(u, s);
        userRepository.addServer(u1, s);
        userRepository.addServer(u2, s);

        Server s1 = new Server("server2");
        s1 = serverRepository.save(s1);

        userRepository.addServer(u3, s1);
        u4 = userRepository.addServer(u4, s1);

        Channel c = new Channel("chan1", (Integer) s.getKey());
        c = ChannelRepository.channelRepository.save(c);

        Channel c2 = new Channel("chan2", (Integer) s1.getKey());
        c2 = ChannelRepository.channelRepository.save(c2);

        Message m = new Message("message1", new Date(), (String) u4.getKey(), (Integer) c2.getKey());
        m = MessageRepository.messageRepository.save(m);
        System.out.println("Seed success");
    }
}
