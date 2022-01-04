package models;

import composite.CompositeChannelSingleton;
import composite.CompositeUserSingleton;

import java.util.Date;

public class Message extends AbstractModel {
    private final String content;
    private final Date date;

    public Message(String content, Date date, String userPseudo, Integer idChannel) {
        this.content = content;
        this.date = date;
        this.getManyToOneReferences().put("user", userPseudo);
        this.getManyToOneReferences().put("channel", idChannel);
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public Channel getChannel() {
        return CompositeChannelSingleton.compositeChannelSingleton
                .get((Integer) this.getManyToOneReferences().get("channel"));
    }

    public User getUser() {
        return CompositeUserSingleton.compositeUserSingleton
                .get((String) this.getManyToOneReferences().get("user"));
    }

    @Override
    public String toString() {
        return String.format("""
                            Message:
                                content: %s
                                date: %s
                                %s
                """, content, date, getUser().toStringWithoutRelation());
    }

    public String toStringWithoutRelation() {
        return String.format("""
                %s - %s: %s
                """, date, getUser().toStringWithoutRelation(), content);
    }
}
