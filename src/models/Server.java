package models;

import composite.CompositeChannelSingleton;
import composite.CompositeUserSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends AbstractModel {
    private AtomicInteger userCounter = new AtomicInteger(0);
    private final String name;
    private final List<Channel> channels = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        this.users.clear();
        for (Object ref : this.getManyToManyReferences().get("user")) {
            this.users.add(CompositeUserSingleton.compositeUserSingleton.get((String) ref));
        }

        return users;
    }

    public Server(String name) {
        this.name = name;
    }

    public List<Channel> getChannels() {
        channels.clear();
        for (Object ref : this.getOneToManyReferences().get("channels")) {
            this.channels.add(CompositeChannelSingleton.compositeChannelSingleton.get((Integer) ref));
        }

        return channels;
    }

    public int getUserCounter() {
        return userCounter.get();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Server { "
            + "userCounter=" + userCounter
            + ", name='" + name
            + "', channels=" + getChannels()
            + ", users=" + users + " }";
    }
}
