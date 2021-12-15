package models;

import java.util.ArrayList;

public class Channel extends AbstractModel {
    private final String name;
    private final ArrayList<Message> messages = new ArrayList<>();
    private final Server server;

    public Channel(String name, Server server) {
        this.name = name;
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }
}
