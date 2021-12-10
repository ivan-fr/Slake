package models;

import java.util.ArrayList;

public class Server extends AbstractModel {
    private Integer userCounter = 0;
    private final String name;
    private final ArrayList<Channel> channels = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public Server(Integer userCounter, String name) {
        this.userCounter = userCounter;
        this.name = name;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Integer getUserCounter() {
        return userCounter;
    }

    public String getName() {
        return name;
    }
}
