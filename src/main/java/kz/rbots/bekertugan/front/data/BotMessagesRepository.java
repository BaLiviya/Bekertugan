package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.BotMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;
@Repository
public interface BotMessagesRepository extends CrudRepository<BotMessage, Long> {

    List<BotMessage> findAllByChatId(Long chatID);
}
