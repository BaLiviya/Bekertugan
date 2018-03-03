package kz.rbots.bekertugan.broadcaster;

import kz.rbots.bekertugan.entities.Dialog;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster implements Serializable {
    private static final long serialVersionUID = 1L;

    private static ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    public interface BotUpdatesListener {
        void receiveBroadcast(Update update);
    }

    public interface TelegramMessageSender {
        void receiveBroadcast(SendMessage sendMessage);
    }

    public interface TelegramDialogsUpdateListener {
        void receiveBroadcast(Dialog dialog, String botToken);
    }

    private static LinkedList<BotUpdatesListener> botListeners = new LinkedList<>();
    private static LinkedList<TelegramMessageSender> telegramListeners = new LinkedList<>();
    private static LinkedList<TelegramDialogsUpdateListener> dialogsListeners = new LinkedList<>();

    public static synchronized void register(
            BotUpdatesListener listener) {
        botListeners.add(listener);
    }

    public static synchronized void unregister(
            BotUpdatesListener listener) {
        botListeners.remove(listener);
    }

    public static synchronized void registerDialogListener(
            TelegramDialogsUpdateListener telegramDialogsUpdateListner){
        dialogsListeners.add(telegramDialogsUpdateListner);
    }

    public static synchronized void newDialog(
            final Dialog dialog,String token) {
        for (final TelegramDialogsUpdateListener listener: dialogsListeners)
            executorService.execute(() -> listener.receiveBroadcast(dialog,token));
    }

    public static synchronized void broadcast(
            final Update update) {
        for (final BotUpdatesListener listener: botListeners)
            executorService.execute(() -> listener.receiveBroadcast(update));
    }

    public static synchronized void broadcastToBot(
            final SendMessage sendMessage) {
        for (final TelegramMessageSender uiListeners: telegramListeners)
            executorService.execute(() -> uiListeners.receiveBroadcast(sendMessage));
    }

    public static synchronized void registerBot(
            TelegramMessageSender listener) {
        telegramListeners.add(listener);
    }

    public static synchronized void unregisterBot(
            TelegramMessageSender listener) {
        telegramListeners.remove(listener);
    }

    public static synchronized void unregisterDialogsListener(
            TelegramDialogsUpdateListener listener) {
        telegramListeners.remove(listener);
    }
}
