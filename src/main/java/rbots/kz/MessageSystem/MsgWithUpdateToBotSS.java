package rbots.kz.MessageSystem;

import org.telegram.telegrambots.api.objects.Update;
import rbots.kz.BotSessionService.BotSessionService;

public class MsgWithUpdateToBotSS extends MsgToBotSS {
    private Update update;
    private long   chatId;

    public MsgWithUpdateToBotSS(Address from, Address to, Update update, long chatId) {
        super(from, to);
        this.update = update;
        this.chatId = chatId;
    }

    @Override
    void exec(BotSessionService botSessionService) {
        botSessionService.handleUpdate(update,chatId);
    }
}
