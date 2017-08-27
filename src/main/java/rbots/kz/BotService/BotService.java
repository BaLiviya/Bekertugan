package rbots.kz.BotService;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import rbots.kz.BotSessionService.BotSessionService;
import rbots.kz.DBService.DBEntity.BotMessage;
import rbots.kz.MessageSystem.*;

@Slf4j
public class BotService implements Abonent, Runnable {
    private Address       address;
    private MessageSystem ms;
    private Bot           bot;


    public BotService(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            bot = new Bot(this);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            throw new RuntimeException(e);
        }
        ms.addService(this);
    }

    @Override
    public void run() {
        while (true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(10);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    void sendMessageToSessionService(Update update, long chatId){
       Address address = ms.getAddressService().getAddress(BotSessionService.class);
       ms.sendMessage(new MsgWithUpdateToBotSS(getAddress(),address,update,chatId));
    }

    public void sendBlad(long chatId, BotMessage botMessage){
        try {
            bot.execute(botMessage.getSendMessage().setChatId(chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
