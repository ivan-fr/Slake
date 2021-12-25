package models;

import composite.CompositeChannelSingleton;
import composite.CompositeUserSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Server extends AbstractModel {
    private final AtomicInteger userCounter = new AtomicInteger(0);
    private final String name;
    private final List<Channel> channels = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        this.users.clear();
        for (Object ref : this.getManyToManyReferences().get("user")) {
            this.users.add(CompositeUserSingleton.compositeUserSingleton.get((String) ref));
        }

        return users;
    }

    public Server(String name) {
        this.name = name;
    }

    public List<Channel> getChannels() {
        channels.clear();
        for (Object ref : this.getOneToManyReferences().get("channels")) {
            this.channels.add(CompositeChannelSingleton.compositeChannelSingleton.get((Integer) ref));
        }

        return channels;
    }

    public List<String> getChannelsNames() {
        List<Channel> channels = getChannels() ;
        List<String> names = new ArrayList<>() ;
        for (Channel channel : channels) {
            names.add(channel.getName()) ;
        }
        return names;
    }

    public void showChannels() {
        List<String> names = getChannelsNames() ;
        for (String name : names) {
            System.out.println("    " + name);
        }
    }

    public boolean gotChannel(String name) {
        for (Channel channel: channels) {
            if(name.equals(channel.getName())) return true;
        }
        return false;
    }


    public int getUserCounter() {
        return userCounter.get();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("""
                            Server's name: %s
                            %s
                        """, name, getChannels().stream()
                        .map(Channel::toString)
                        .collect(Collectors.joining("", "", "")));
    }

    public String toStringWithoutRelation() {
        return String.format("Server's name: %s", name);
    }
}
