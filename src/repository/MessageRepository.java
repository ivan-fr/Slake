package repository;

import models.Message;

import java.util.ArrayList;

public class MessageRepository implements IRepository<Message, Integer> {
    @Override
    public Message save(Message object) {
        return null;
    }

    @Override
    public Message get(Integer key) {
        return null;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public Message update(Message object) {
        return null;
    }

    @Override
    public ArrayList<Message> list() {
        return null;
    }
}
