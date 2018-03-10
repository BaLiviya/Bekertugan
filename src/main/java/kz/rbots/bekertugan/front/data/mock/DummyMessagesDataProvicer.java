package kz.rbots.bekertugan.front.data.mock;

import kz.rbots.bekertugan.entities.BotMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class DummyMessagesDataProvicer {

    private static List<BotMessage> botMessages = new LinkedList<>();

    public static void addMessage(BotMessage thatMessage){
       botMessages.add(thatMessage);
    }

    public static Stream<BotMessage> getAllMessagesFromUser(long userId){
        return botMessages.stream().filter(m -> m.getChatId()==userId);
    }


}
