package models;

import composite.CompositeServerSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User extends AbstractModel {
    private final String pseudo;
    private String currentServer=null;
    private String currentChannel = null;
    private final ArrayList<Server> servers = new ArrayList<>();

    public ArrayList<Server> getServers() {
        this.servers.clear();
        for (Object ref : this.getManyToManyReferences().get("servers")) {
            this.servers.add(CompositeServerSingleton.compositeServerSingleton.get((Integer) ref));
        }
        return servers;
    }

    public Server getServer(String name) {
        for (Server server:servers) {
            if (name.equals(server.getName())) return server;
        }
        return null;
    }


    public void showServers() {
        List<Server> name = getServers();
        for (Server server: servers) {
            System.out.println("- " + server.getName());
            server.showChannels();
        }
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public String getPseudo() {
        return pseudo;
    }

    public User(String pseudo) {
        this.pseudo = pseudo;
    }


    @Override
    public String toString() {
        return  String.format("""
               User's pseudo: %s
               %s
               """, pseudo, getServers().stream()
                .map(Server::toString)
                .collect(Collectors.joining("", "", "")));
    }

    public String toStringWithoutRelation() {
        return  String.format("User's pseudo: %s", pseudo);
    }
}
