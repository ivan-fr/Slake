package models;

import java.util.ArrayList;

public class Room {
    private final Integer roomId;
    private final String name;
    private Integer userCounter = 0;
    private final ArrayList<Message> messages = new ArrayList<>();

    public Room(Integer roomId, String name, Integer userCounter) {
        this.roomId = roomId;
        this.name = name;
        this.userCounter = userCounter;
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

    public Integer getUserCounter() {
        return userCounter;
    }
}
