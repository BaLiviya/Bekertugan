package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.Dialog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
    boolean existsByChatId(Long chatId);

    @Query(value = "select * from dialog ORder BY (\n" +
            "  SELECT max(message_id) FROM bot_message WHERE bot_message.chat_id = dialog.chat_id) DESC LIMIT :limit",
            nativeQuery = true)
    Iterable<Dialog> findSomeFirstByOrderMessageISNewer(@Param("limit") int limit);

    @Query(value = "select * from dialog ORder BY (SELECT max(message_id) FROM bot_message WHERE bot_message.chat_id = dialog.chat_id)" +
            " DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    Iterable<Dialog> findAllByOffset(@Param("limit") int limit, @Param("offset") int offset);


}
