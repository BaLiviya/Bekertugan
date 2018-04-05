package kz.rbots.bekertugan.telegrambot.data;

import kz.rbots.bekertugan.telegrambot.entity.fundamental.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Long> {
}
