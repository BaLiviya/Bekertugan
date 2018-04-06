package kz.rbots.bekertugan.telegrambot.command;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import org.telegram.telegrambots.api.objects.Update;

public abstract class AbstractCommand {

    protected long        chatId;
    protected String      arguments;

    public AbstractCommand(long chatId, String arguments){

        this.chatId    = chatId;

        this.arguments = arguments;


    }

    public abstract boolean execute(Update update, TelegramBot bot);

}
