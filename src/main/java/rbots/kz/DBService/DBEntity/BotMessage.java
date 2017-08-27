package rbots.kz.DBService.DBEntity;

import lombok.Getter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
@Getter
public class BotMessage {
    private long id;
    private SendPhoto sendPhoto;
    private SendMessage sendMessage;


    public BotMessage(long id,SendPhoto sendPhoto, SendMessage message) {
        this.id               = id;
        this.sendMessage      = message;
        this.sendPhoto        = sendPhoto;
    }




}
