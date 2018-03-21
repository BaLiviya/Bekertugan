package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.BotMessage;
import org.springframework.data.repository.CrudRepository;

public interface BotMessagesRepository extends CrudRepository<BotMessage, Long> {
}
