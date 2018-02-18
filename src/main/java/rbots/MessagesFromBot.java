package rbots;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MessagesFromBot {
    private static List<String> botMessages = new ArrayList<>();
    private static List<String> forBotMessages = new ArrayList<>();
    private static  TelegramBot bot;

    public static void setTelegramoBot(TelegramBot telegramBot){
        bot = telegramBot;
    }
//ЕБАНЫЕ КОСТЫЛИ
    public static String getAndDeleteMessages(){
        if (!botMessages.isEmpty()){
            String message = botMessages.get(0);
            botMessages.remove(0);
            return message;
        } else {
            return null;
        }
    }

    public static void putMessage(String message){
        botMessages.add(message);
    }

    public static void sendMessageToBot(Long target, String message){
        try {
            System.out.println(target);
            System.out.println(message);
            SendMessage sendMessage = new SendMessage(target,message);
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



}
