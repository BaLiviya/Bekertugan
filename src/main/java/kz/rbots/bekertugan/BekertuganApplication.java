package kz.rbots.bekertugan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.telegram.telegrambots.ApiContextInitializer;

@EntityScan(
		basePackageClasses = {BekertuganApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class BekertuganApplication {

	public static void main(String[] args) {

		ApiContextInitializer.init();

		SpringApplication.run(BekertuganApplication.class, args);

	}
}
