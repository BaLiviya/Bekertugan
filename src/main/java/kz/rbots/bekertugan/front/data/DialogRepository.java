package kz.rbots.bekertugan.front.data;

import kz.rbots.bekertugan.entities.Dialog;
import org.springframework.data.repository.CrudRepository;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
    public boolean existsByChatId(Long chatId);

}
