package models;

import composite.CompositeMessageSingleton;
import composite.CompositeServerSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return String.format("""
                    Channel's name: %s
                %s
                """, name, getMessages().stream()
                .map(Message::toString)
                .collect(Collectors.joining("", "", "")));
    }

    public String toStringWithoutRelation() {
        return String.format("Channel's name: %s", name);
    }
}
