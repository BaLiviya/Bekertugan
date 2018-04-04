package kz.rbots.bekertugan.telegrambot.utils;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import lombok.Setter;

import java.util.concurrent.*;

public class TelegramBotExecutorUtil {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    @Setter
    private static TelegramBot telegramBot;

    public static void Execute(Runnable runnable){
        pool.execute(runnable);
    }

    //Here is problem with chats, bot can't get avatar of chat, or i don't know how to do this
    public static String getActualURLForAvatar(Integer userId){
       final Future<String> stringFuture = pool.submit(() -> telegramBot.getFilePathById(
               telegramBot.getLastAvatarFileId(userId)));

        try {
            if (stringFuture.get()!=null) {
                return "https://api.telegram.org/file/bot"
                        + telegramBot.getBotToken() + "/" + stringFuture.get();
            } else return null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFilePathById(String fileId) {
       final Future<String> path = pool.submit(() -> telegramBot.getFilePathById(fileId));

        try {
            return path.get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }


}
