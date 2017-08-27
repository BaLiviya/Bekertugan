package rbots.kz.BotSessionService;

import org.telegram.telegrambots.api.objects.Update;

class Conversation {
    private final BotSessionService botSessionService;

    Conversation(BotSessionService botSessionService) {
        this.botSessionService = botSessionService;
    }

    void handleUpdate(Update update, long chatId, BotSessionService botSessionService){
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("блядь")) {
            botSessionService.sendBlad(chatId,1);
        }
    }
}
