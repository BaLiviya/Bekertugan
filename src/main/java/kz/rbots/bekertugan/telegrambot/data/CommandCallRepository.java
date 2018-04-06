package kz.rbots.bekertugan.telegrambot.data;

import kz.rbots.bekertugan.telegrambot.entity.fundamental.CommandCall;
import org.springframework.data.repository.CrudRepository;

public interface CommandCallRepository extends CrudRepository<CommandCall, Integer> {


}
