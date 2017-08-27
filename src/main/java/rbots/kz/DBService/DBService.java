package rbots.kz.DBService;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import rbots.kz.BotService.BotService;
import rbots.kz.DBService.DBEntity.BotMessage;
import rbots.kz.MessageSystem.*;

public class DBService implements Abonent, Runnable {
    private Address       address;
    private MessageSystem ms;

    public DBService(MessageSystem ms) {
        this.address = new Address();
        this.ms = ms;
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

    public void sendBlad(long chatId, long messageId){
        if (messageId == 1) {
            BotMessage botMessage = new BotMessage(1,null,new SendMessage().setText("блад"));
            Address front = ms.getAddressService().getAddress(BotService.class);
            ms.sendMessage(new MsgSendBlad(getAddress(),front,botMessage,chatId));
        }
    }
}
