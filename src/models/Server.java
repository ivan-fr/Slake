package models;

import composite.CompositeChannelSingleton;
import composite.CompositeUserSingleton;

import java.util.ArrayList;

public class Server extends AbstractModel {
    private Integer userCounter = 0;
    private final String name;
    private final ArrayList<Channel> channels = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers() {
        this.users.clear();
        for (Object ref : this.getManyToManyReferences().get("user")) {
            this.users.add(CompositeUserSingleton.compositeUserSingleton.get((String) ref));
        }

        return users;
    }

    public Server(String name) {
        this.name = name;
    }

    public ArrayList<Channel> getChannels() {
        this.channels.clear();
        for (Object ref : this.getManyToManyReferences().get("server")) {
            this.channels.add(CompositeChannelSingleton.compositeChannelSingleton.get((Integer) ref));
        }

        return channels;
    }

    public Integer getUserCounter() {
        return userCounter;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Server { "
            + "userCounter=" + userCounter
            + ", name='" + name
            + "', channels=" + channels
            + ", users=" + users + " }";
    }
}
