package models;

import java.util.Date;

public class Message extends AbstractModel {
    private final String content;
    private final Date date;
    private final Channel channel;
    private final User user;

    public Message(String content, Date date, Channel channel, User user) {
        this.content = content;
        this.date = date;
        this.channel = channel;
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }
}
