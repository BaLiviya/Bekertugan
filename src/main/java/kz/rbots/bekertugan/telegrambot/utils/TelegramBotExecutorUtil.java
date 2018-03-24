package kz.rbots.bekertugan.telegrambot.utils;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import lombok.Setter;
import java.util.concurrent.*;

public class TelegramBotExecutorUtil {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    @Setter
    private static TelegramBot telegramBot;


    public static String getActualURLForAvatar(Integer userId){
       final Future<String> stringFuture = pool.submit(() -> telegramBot.getFilePathById(
               telegramBot.getLastAvatarFileId(userId)));
        try {
            return "https://api.telegram.org/file/bot"
                    + telegramBot.getBotToken() + "/" + stringFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


}
