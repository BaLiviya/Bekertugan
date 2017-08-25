package rbots.kz;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@Slf4j
@SpringBootApplication
public class PersonalAccountApplication {

    public static void main(String[] args) {
        log.info("Start the application");
        SpringApplication.run(PersonalAccountApplication.class, args);
    }
}
