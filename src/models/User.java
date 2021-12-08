package models;

import java.util.ArrayList;

public class User {
    private final Integer userId;
    private final String pseudo;
    private final ArrayList<Message> messages = new ArrayList<>();
    private final ArrayList<Room> rooms = new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Integer getUserId() {
        return userId;
    }

    public User(Integer userId, String pseudo) {
        this.userId = userId;
        this.pseudo = pseudo;
    }
}
