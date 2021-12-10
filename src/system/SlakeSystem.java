package system;

import models.Channel;
import models.Message;
import models.Server;
import models.User;

import java.util.HashMap;

public class SlakeSystem {
    private HashMap<Integer, Channel> channelMap;
    private HashMap<Integer, Message> messageMap;
    private HashMap<Integer, User> userMap;
    private HashMap<Integer, Server> serverMap;

    static public final SlakeSystem slakeSystem = new SlakeSystem();

    private SlakeSystem() {
    }

    public HashMap<Integer, Channel> getChannelMap() {
        return channelMap;
    }

    public HashMap<Integer, Message> getMessageMap() {
        return messageMap;
    }

    public HashMap<Integer, User> getUserMap() {
        return userMap;
    }

    public HashMap<Integer, Server> getServerMap() {
        return serverMap;
    }
}
