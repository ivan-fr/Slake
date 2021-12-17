package composite;

import cache.CacheRepository;
import models.Channel;
import repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class CompositeChannelSingleton implements IComposite<Channel, Integer> {
    public final static CompositeChannelSingleton compositeChannelSingleton = new CompositeChannelSingleton();
    private final CacheRepository<Channel> channelCacheRepository = new CacheRepository<>();

    private CompositeChannelSingleton() {

    }

    @Override
    public Channel save(Channel object) {
        Channel savedChannel = ChannelRepository.channelRepository.save(object);
        if (savedChannel != null) {
            channelCacheRepository.save(savedChannel);
        }
        return savedChannel;
    }

    @Override
    public Channel get(Integer key) {
        if (channelCacheRepository.get(key) == null) {
            Channel u = ChannelRepository.channelRepository.get(key);
            channelCacheRepository.save(u);
            return u;
        }

        return channelCacheRepository.get(key);
    }

    @Override
    public boolean delete(Integer key) {
        if (ChannelRepository.channelRepository.delete(key)) {
            if (channelCacheRepository.get(key) != null) {
                channelCacheRepository.delete(channelCacheRepository.get(key));
            }

            return true;
        }

        return false;
    }

    @Override
    public Channel update(Channel object) {
        Channel newChannel = ChannelRepository.channelRepository.update(object);
        channelCacheRepository.update(object, newChannel);
        return newChannel;
    }

    @Override
    public List<Channel> list() {
        return channelCacheRepository.list();
    }

    @Override
    public void hydrate() {
        ArrayList<Channel> channels = ChannelRepository.channelRepository.list();
        channelCacheRepository.clear();

        for (Channel channel : channels) {
            channelCacheRepository.save(channel);
        }
    }
}
