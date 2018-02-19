package kz.rbots.bekertugan;

import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.telegrambot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@SpringBootApplication
public class BekertuganApplication {

	public static void main(String[] args) {
		SpringApplication.run(BekertuganApplication.class, args);
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		TelegramBot bot = new TelegramBot();
		try {
			telegramBotsApi.registerBot(bot);
			System.out.println("Bot is online");
			Broadcaster.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

	}
}
