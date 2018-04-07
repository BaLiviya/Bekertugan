package kz.rbots.bekertugan.telegrambot.command.impl;

import kz.rbots.bekertugan.telegrambot.TelegramBot;
import kz.rbots.bekertugan.telegrambot.command.AbstractCommand;
import kz.rbots.bekertugan.telegrambot.data.MessageRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Message;
import kz.rbots.bekertugan.telegrambot.service.KeyboardService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Slf4j
public class SendMessageFromDBWithKeyboard extends AbstractCommand {

    private ReplyKeyboard     keyboard;

    private Message message;

    public SendMessageFromDBWithKeyboard(long chatId, String arguments) {

        super(chatId, arguments);

        MessageRepository messageRepository = TelegramBotRepositoryProvider.getMessageRepository();

        KeyboardService keyboardService = new KeyboardService();

        try {

            message = messageRepository.findOne(Long.valueOf(arguments));


        } catch (NumberFormatException e) {

            log.warn("Illegal argument for message");

            log.warn(e.toString());

        }

        try {

            keyboard = keyboardService.getReplyKeyboardById(message.getKeyboardId());

        } catch (NumberFormatException e) {

            log.warn("Illegal id for keyboard from message with ID - " + message.getId() );

            log.warn(e.toString());

        }

    }

    @Override
    public boolean execute(Update update, TelegramBot bot) {

        try {

            bot.execute(new SendMessage(chatId,message.getMessageText()).setReplyMarkup(keyboard));

        } catch (TelegramApiException e) {

            e.printStackTrace();

        }

        return true;
    }
}
