package models;

import java.util.ArrayList;

public class Channel extends AbstractModel {
    private final String name;
    private final ArrayList<Message> messages = new ArrayList<>();
    private final Server server = null;

    public Channel(String name) {
        this.name = name;
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
