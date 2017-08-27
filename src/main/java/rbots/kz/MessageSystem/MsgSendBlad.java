package rbots.kz.MessageSystem;

import rbots.kz.BotService.BotService;
import rbots.kz.DBService.DBEntity.BotMessage;

public class MsgSendBlad extends MsgToBotFront {
    private BotMessage botMessage;
    private long       chatId;
    public MsgSendBlad(Address from, Address to, BotMessage botMessage, long chatId) {
        super(from, to);
        this.botMessage = botMessage;
        this.chatId     = chatId;
    }


    @Override
    void exec(BotService botService) {
        botService.sendBlad(chatId,botMessage);
    }
}
