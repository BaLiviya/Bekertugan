package kz.rbots.bekertugan.telegrambot.data;

import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ButtonRepository extends CrudRepository<Button, Long> {

    Button findFirstByButtonText(String buttonText);

    List<Button> findAllByOrderByIdAsc();

}
