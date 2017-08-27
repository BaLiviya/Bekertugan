package rbots.kz.BotSessionService;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.objects.Update;
import rbots.kz.DBService.DBService;
import rbots.kz.MessageSystem.*;

import java.util.HashMap;
import java.util.Map;

public class BotSessionService implements Abonent, Runnable {
    private Address address;
    private MessageSystem ms;
    private Map<Long, Conversation> conversations = new HashMap<>();


    public BotSessionService(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void handleUpdate(Update update, long chatId){
        if (chatId < 0) {
        handleGroupUpdates(update, chatId);
        } else {
            Conversation conversation = getConversation(chatId);
            conversation.handleUpdate(update,chatId,this);
        }

    }

    private void handleGroupUpdates(Update update, long chatId){
//Todo make some things
    }

    private Conversation getConversation(Long chatId) {
        Conversation conversation = conversations.get(chatId);
        if (conversation == null) {
//            log.info("Создана новая беседа для '{}'", chatId);
            conversation = new Conversation(this);
            conversations.put(chatId, conversation);
        }
        return conversation;
    }

    @Override
    public void run() {
        while (true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(10);
        }
    }

    void sendBlad(long chatId, int messageId){
        Address address = ms.getAddressService().getAddress(DBService.class);
        ms.sendMessage(new MsgToSendMsgFromDB(getAddress(),address,chatId,messageId));
    }


}
