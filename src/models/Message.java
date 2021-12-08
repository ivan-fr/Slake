package models;

import java.util.Date;

public class Message {
    private final String content;
    private final Date date;
    private final Integer roomId;
    private final Channel channel;

    public Message(String content, Date date, Integer roomId, Channel room) {
        this.content = content;
        this.date = date;
        this.roomId = roomId;
        this.channel = room;
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
}
