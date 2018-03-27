package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.Dialog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
    boolean existsByChatId(Long chatId);

    @Query(value = "select * from dialog ORder BY (\n" +
            "  SELECT max(message_id) FROM bot_message WHERE bot_message.chat_id = dialog.chat_id) DESC ",
            nativeQuery = true)
    Iterable<Dialog> findAllByOrderMessageISNewer();


}
