package models;

import java.util.Date;

public class Message extends AbstractModel {
    private final String content;
    private final Date date;
    private final Channel channel = null;
    private final User user = null;

    public Message(String content, Date date) {
        this.content = content;
        this.date = date;
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
