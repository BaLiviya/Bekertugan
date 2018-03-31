package kz.rbots.bekertugan.telegrambot.utils;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import lombok.Setter;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.concurrent.*;

public class TelegramBotExecutorUtil {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    @Setter
    private static TelegramBot telegramBot;

    public static void Execute(Runnable runnable){
        pool.execute(runnable);
    }

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

    public static String getFilePathById(String fileId) {
       final Future<String> path = pool.submit(() -> telegramBot.getFilePathById(fileId));

        try {
            return path.get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
//        GetFile getFile = new GetFile();
//        getFile.setFileId(fileId);
//        try {
//            return telegramBot.execute(getFile).getFilePath();
//        } catch (TelegramApiException ignored) {
//            return null;
//        }
    }


}
