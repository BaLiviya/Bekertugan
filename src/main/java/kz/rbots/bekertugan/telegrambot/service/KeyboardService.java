package kz.rbots.bekertugan.telegrambot.service;

import kz.rbots.bekertugan.telegrambot.data.ButtonRepository;
import kz.rbots.bekertugan.telegrambot.data.KeyboardRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Keyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardService {

    private ButtonRepository   btnRepo = TelegramBotRepositoryProvider.getButtonRepository();

    private KeyboardRepository keyRepo = TelegramBotRepositoryProvider.getKeyboardRepository();

    public ReplyKeyboard getReplyKeyboardById(int id){

        Keyboard keyboard = keyRepo.findOne(id);

        String[] rows = keyboard.getContainedButtons().split(";");

        //Very ugly solution, need rework and full refactor, but now i'm sick and don't wanna care about that
        //Call db every time when we want button - it's bad, need make beautiful solution here
        //TODO
        if (keyboard.isInline()){

            return generateInlineKeyboard(rows);

        } else {

            return generateReplyMarkup(rows);

        }


    }

    private InlineKeyboardMarkup generateInlineKeyboard(String[] rows){

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        Arrays.stream(rows).forEach((row)->{

            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            Arrays.stream(row.split(",")).forEach((buttonId)->{

                Button btn = btnRepo.findOne(Long.valueOf(buttonId));

                InlineKeyboardButton inlineButton = new InlineKeyboardButton(btn.getButtonText());

                if (!btn.getURL().isEmpty()) inlineButton.setUrl(btn.getURL());

                if (!btn.getCallbackData().isEmpty()) {

                    inlineButton.setCallbackData(btn.getCallbackData());

                } else {

                    inlineButton.setCallbackData(btn.getButtonText());

                }

                keyboardRow.add(inlineButton);


            });

            keyboardRows.add(keyboardRow);


        });

       return keyboard.setKeyboard(keyboardRows);

    }

    private ReplyKeyboardMarkup generateReplyMarkup(String[] rows){

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setResizeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        Arrays.stream(rows).forEach((row)->{

            KeyboardRow keyboardRow = new KeyboardRow();

            Arrays.stream(row.split(",")).forEach((buttonId) -> {

                Button btn = btnRepo.findOne(Long.valueOf(buttonId));

                KeyboardButton keyboardButton = new KeyboardButton();

                keyboardButton.setText(btn.getButtonText());

                if (!btn.getRequestType().isEmpty()){

                    switch (btn.getRequestType()){

                        case "contact" :

                            keyboardButton.setRequestContact(true);

                            break;

                        case "location" :

                            keyboardButton.setRequestLocation(true);

                            break;

                    }

                }

                keyboardRow.add(keyboardButton);

            });

            keyboardRows.add(keyboardRow);

        });

        return keyboardMarkup.setKeyboard(keyboardRows);

    }


}
