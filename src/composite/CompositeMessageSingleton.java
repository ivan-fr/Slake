package composite;

import cache.CacheRepository;
import models.Message;
import repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class CompositeMessageSingleton implements IComposite<Message, Integer> {
    public final static CompositeMessageSingleton compositeMessageSingleton = new CompositeMessageSingleton();
    private final CacheRepository<Message> messageCacheRepository = new CacheRepository<>();

    private CompositeMessageSingleton() {

    }

    @Override
    public Message save(Message object) {
        Message savedMessage = MessageRepository.messageRepository.save(object);
        if (savedMessage != null) {
            messageCacheRepository.save(savedMessage);
        }
        return savedMessage;
    }

    @Override
    public Message get(Integer key) {
        if (messageCacheRepository.get(key) == null) {
            Message u = MessageRepository.messageRepository.get(key);
            messageCacheRepository.save(u);
            return u;
        }

        return messageCacheRepository.get(key);
    }

    @Override
    public boolean delete(Integer key) {
        if (MessageRepository.messageRepository.delete(key)) {
            if (messageCacheRepository.get(key) != null) {
                messageCacheRepository.delete(messageCacheRepository.get(key));
            }

            return true;
        }

        return false;
    }

    @Override
    public Message update(Message object) {
        Message newMessage = MessageRepository.messageRepository.update(object);
        messageCacheRepository.update(object, newMessage);
        return newMessage;
    }

    @Override
    public List<Message> list() {
        return messageCacheRepository.list();
    }

    @Override
    public void hydrate() {
        ArrayList<Message> messages = MessageRepository.messageRepository.list();
        messageCacheRepository.clear();

        for (Message message : messages) {
            messageCacheRepository.save(message);
        }
    }
}
