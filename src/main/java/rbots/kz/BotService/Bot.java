package rbots.kz.BotService;



import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


public class Bot extends TelegramLongPollingBot {
    private long  chatId;
    private final BotService botService;

    Bot(BotService botService) {
        this.botService = botService;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getFrom().getId();
        }
        else if (update.hasShippingQuery()) {
            chatId = update.getShippingQuery().getFrom().getId();
        }
        else if (update.hasPreCheckoutQuery()) {
            chatId = update.getPreCheckoutQuery().getFrom().getId();
        }
        else if (update.hasChannelPost()) {
            chatId = update.getChannelPost().getChatId();
        }
        sendUpdateToBotSS(update,chatId);
//        .info("New update just sends to bot session service " + LocalDateTime.now().toString());
        chatId = 0;
    }

    //Todo Put Things Here
    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    private void sendUpdateToBotSS(Update update, long chatId){
        botService.sendMessageToSessionService(update,chatId);
    }
}
