package rbots;

import org.atmosphere.config.service.Singleton;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


@Singleton
public class TelegramBot extends TelegramLongPollingBot {



    //И ДАЖЕ ТУТ ЕБАНЫЕ КОСТЫЛИ
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());
            MessagesFromBot.putMessage(update.getMessage().getText());
            MyUI.postMessage(update.getMessage().getFrom().getFirstName() +
                    ":" + update.getMessage().getText());
            MyUI.setLastSender(update.getMessage().getChatId());
        }
    }


    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }
}
