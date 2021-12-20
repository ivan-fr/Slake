package models;

import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;

import java.util.ArrayList;
import java.util.List;

public class Channel extends AbstractModel {
    private final String name;
    private final ArrayList<Message> messages = new ArrayList<>();

    public Channel(String name, Integer idServer) {
        this.name = name;
        this.getManyToOneReferences().put("server", idServer);
    }

    public Server getServer() {
        return CompositeServerSingleton.compositeServerSingleton
                .get((Integer) this.getManyToOneReferences().get("server"));
    }

    public List<Message> getMessages() {
        messages.clear();
        for (Object ref : this.getOneToManyReferences().get("messages")) {
            this.messages.add(CompositeMessageSingleton.compositeMessageSingleton.get((Integer) ref));
        }

        return messages;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Channel { "
                + "name='" + name
                + "', server=" +
                ", messages=" + getMessages() +" }";
    }

    public String toStringWithoutRelation() {
        return "Channel { name='" + name + "}";
    }
}
