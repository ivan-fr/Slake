package models;

import java.util.ArrayList;

public class User {
    private final Integer userId;
    private final String pseudo;
    private final ArrayList<Server> servers = new ArrayList<>();

    public ArrayList<Server> getsServers() {
        return servers;
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", pseudo='" + pseudo + '\'' +
                ", servers=" + servers +
                '}';
    }
}
