package models;

public class Message {
    private final String content;

    private final Integer roomId;
    private final Room room;

    private final Integer user_source_id;
    private final Integer user_destination_id;
    private final User user_source;
    private final User user_destination;

    public Message(String content, Integer roomId, Room room, Integer user_source_id, Integer user_destination_id, User user_source, User user_destination) {
        this.content = content;
        this.roomId = roomId;
        this.room = room;
        this.user_source_id = user_source_id;
        this.user_destination_id = user_destination_id;
        this.user_source = user_source;
        this.user_destination = user_destination;
    }

    public String getContent() {
        return content;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Room getRoom() {
        return room;
    }

    public Integer getUser_source_id() {
        return user_source_id;
    }

    public Integer getUser_destination_id() {
        return user_destination_id;
    }

    public User getUser_source() {
        return user_source;
    }

    public User getUser_destination() {
        return user_destination;
    }
}
