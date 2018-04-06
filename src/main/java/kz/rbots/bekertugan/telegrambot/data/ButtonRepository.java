package kz.rbots.bekertugan.telegrambot.data;

import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;
import org.springframework.data.repository.CrudRepository;

public interface ButtonRepository extends CrudRepository<Button, Long> {

    Button findFirstByButtonText(String buttonText);

}
