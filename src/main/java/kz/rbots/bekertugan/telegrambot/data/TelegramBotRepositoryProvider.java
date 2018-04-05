package kz.rbots.bekertugan.telegrambot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotRepositoryProvider {

    private static ButtonRepository buttonRepository;

    private static MessageRepository messageRepository;
    @Autowired
    public TelegramBotRepositoryProvider(ButtonRepository buttonRepository, MessageRepository messageRepository) {
        setButtonRepository(buttonRepository);
        setMessageRepository(messageRepository);
    }

    private static void setButtonRepository(ButtonRepository buttonRepository) {
        TelegramBotRepositoryProvider.buttonRepository = buttonRepository;
    }

    private static void setMessageRepository(MessageRepository messageRepository) {
        TelegramBotRepositoryProvider.messageRepository = messageRepository;
    }

    public static ButtonRepository getButtonRepository() {
        return buttonRepository;
    }

    public static MessageRepository getMessageRepository() {
        return messageRepository;
    }
}
