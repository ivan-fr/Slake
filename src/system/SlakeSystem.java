package system;

import models.Channel;
import models.Message;
import models.Server;
import models.User;

import java.util.Map;

public class SlakeSystem {
    private Map<Integer, Channel> channelMap;
    private Map<Integer, Message> messageMap;
    private Map<Integer, User> userMap;
    private Map<Integer, Server> serverMap;

    static public final SlakeSystem slakeSystem = new SlakeSystem();

    private SlakeSystem() {
    }

    public Map<Integer, Channel> getChannelMap() {
        return channelMap;
    }

    public Map<Integer, Message> getMessageMap() {
        return messageMap;
    }

    public Map<Integer, User> getUserMap() {
        return userMap;
    }

    public Map<Integer, Server> getServerMap() {
        return serverMap;
    }
}
