package models;

import composite.CompositeServerSingleton;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class User extends AbstractModel {
    private final String pseudo;
    private final ArrayList<Server> servers = new ArrayList<>();

    public ArrayList<Server> getServers() {
        this.servers.clear();
        for (Object ref : this.getManyToManyReferences().get("servers")) {
            this.servers.add(CompositeServerSingleton.compositeServerSingleton.get((Integer) ref));
        }
        return servers;
    }

    public String getPseudo() {
        return pseudo;
    }

    public User(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return String.format("""
                User's pseudo: %s
                %s
                """, pseudo, getServers().stream()
                .map(Server::toString)
                .collect(Collectors.joining("", "", "")));
    }

    public String toStringWithoutRelation() {
        return String.format("%s", pseudo);
    }
}
