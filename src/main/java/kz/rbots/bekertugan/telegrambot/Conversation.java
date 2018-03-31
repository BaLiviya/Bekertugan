package kz.rbots.bekertugan.telegrambot;


import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;


@RequiredArgsConstructor
@Slf4j
class Conversation {
    private final boolean isGroup;
    private final TelegramBot bot;
    private final Long chatId;
    private final DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();

    void operate(Update update){
        try {
            // Test
            bot.execute(new SendMessage(chatId,"yo"));
        } catch (TelegramApiException e) {
            log.warn(e.toString());
            e.printStackTrace();
        }
    }



}
