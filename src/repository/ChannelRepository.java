package repository;

import models.Channel;

import java.util.ArrayList;

public class ChannelRepository implements IRepository<Channel, Integer> {
    @Override
    public Channel save(Channel object) {
        return null;
    }

    @Override
    public Channel get(Integer key) {
        return null;
    }

    @Override
    public boolean delete(Integer key) {
        return false;
    }

    @Override
    public Channel update(Channel object) {
        return null;
    }

    @Override
    public ArrayList<Channel> list() {
        return null;
    }
}
