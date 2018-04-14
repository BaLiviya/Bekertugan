package kz.rbots.bekertugan.telegrambot.data;

import kz.rbots.bekertugan.telegrambot.entity.fundamental.Keyboard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KeyboardRepository extends CrudRepository<Keyboard, Integer> {

   List<Keyboard> findAllByOrderByIdAsc();

}
