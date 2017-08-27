package rbots.kz.MessageSystem;

import rbots.kz.DBService.DBService;

public class MsgToSendMsgFromDB extends MsgToDB {
    private long chatId;
    private int  messageId;
    public MsgToSendMsgFromDB(Address from, Address to, long chatId, int messageId) {
        super(from, to);
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    void exec(DBService dbService) {
        dbService.sendBlad(chatId,messageId);
    }
}
