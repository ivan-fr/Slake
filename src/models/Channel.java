package models;

import composite.CompositeServerSingleton;

import java.util.ArrayList;

public class Channel extends AbstractModel {
    private final String name;

    public Channel(String name, Integer idServer) {
        this.name = name;
        this.getManyToOneReferences().put("server", idServer);
    }

    public Server getServer() {
        return CompositeServerSingleton.compositeServerSingleton
                .get((Integer) this.getManyToOneReferences().get("server"));
    }

    public ArrayList<Message> getMessages() {
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Channel { "
                + "name='" + name
                + "', server=" + getServer() + " }";
    }
}
