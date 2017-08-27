package rbots.kz;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rbots.kz.BotService.BotService;
import rbots.kz.BotSessionService.BotSessionService;
import rbots.kz.DBService.DBService;
import rbots.kz.MessageSystem.MessageSystem;

import java.util.Base64;

@Slf4j
@SpringBootApplication
public class PersonalAccountApplication {

    public static void main(String[] args) {
        MessageSystem ms = new MessageSystem();
        BotService botService = new BotService(ms);
        BotSessionService botSessionService = new BotSessionService(ms);
        DBService dbService = new DBService(ms);
        (new Thread(botService)).start();
        (new Thread(botSessionService)).start();
        (new Thread(dbService)).start();

        log.info("Start the application");
        SpringApplication.run(PersonalAccountApplication.class, args);
    }
}
