package models;

import java.util.Date;

public class Message extends AbstractModel {
    private final String content;
    private final Date date;
    private final Integer roomId;
    private final Channel channel;
    private final User user;

    public Message(String content, Date date, Integer roomId, Channel room, User user) {
        this.content = content;
        this.date = date;
        this.roomId = roomId;
        this.channel = room;
        this.user = user;
    }

    public Date getDate() {
        return date;
    }
    public String getContent() {
        return content;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }
}
