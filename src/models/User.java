package models;

import composite.CompositeServerSingleton;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class User extends AbstractModel {
    private final String username;
    private final String password;
    private final ArrayList<Server> servers = new ArrayList<>();

    public ArrayList<Server> getServers() {
        this.servers.clear();
        for (Object ref : this.getManyToManyReferences().get("servers")) {
            this.servers.add(CompositeServerSingleton.compositeServerSingleton.get((Integer) ref));
        }
        return servers;
    }
    
    public User(String username, String password) {
        this.setKey(username);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format(
            """
            User {
                username = %s
                password = ******
                servers = %s
            }
            """,
            username,
            getServers().stream().map(Server::toString)
            .collect(Collectors.joining("", "", ""))
        );
    }

    public String toStringWithoutRelation() {
        return String.format("%s", username);
    }
}
