package models;

import composite.CompositeServerSingleton;

import java.util.ArrayList;

public class User extends AbstractModel {
    private final String pseudo;
    private final ArrayList<Server> servers = new ArrayList<>();

    public ArrayList<Server> getServers() {
        this.servers.clear();
        for (Object ref : this.getManyToManyReferences().get("server")) {
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
        return "User { "
                + "pseudo='" + pseudo
                + ", servers=" + getServers() + " }";
    }
}
