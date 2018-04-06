package kz.rbots.bekertugan.telegrambot.command.impl;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import kz.rbots.bekertugan.telegrambot.command.AbstractCommand;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class SendMessageWithTextFromArguments extends AbstractCommand {
    public SendMessageWithTextFromArguments(long chatId, String arguments) {
        super(chatId, arguments);
    }

    @Override
    public boolean execute(Update update, TelegramBot bot) {
        try {
            bot.execute(new SendMessage(chatId, arguments));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }
}
