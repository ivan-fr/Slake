package models;

import java.util.ArrayList;

public class Channel extends AbstractModel {
    private final Integer roomId;
    private final String name;
    private final ArrayList<Message> messages = new ArrayList<>();
    private final Server server;

    public Channel(Integer roomId, String name, Integer userCounter, Server server) {
        this.roomId = roomId;
        this.name = name;
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
    public Integer getRoomId() {
        return roomId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }
}
