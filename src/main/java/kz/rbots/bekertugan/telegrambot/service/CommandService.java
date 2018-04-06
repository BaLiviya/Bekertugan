package kz.rbots.bekertugan.telegrambot.service;

import kz.rbots.bekertugan.telegrambot.command.AbstractCommand;
import kz.rbots.bekertugan.telegrambot.command.CommandFactory;
import kz.rbots.bekertugan.telegrambot.data.ButtonRepository;
import kz.rbots.bekertugan.telegrambot.data.CommandCallRepository;
import kz.rbots.bekertugan.telegrambot.data.TelegramBotRepositoryProvider;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.Button;
import kz.rbots.bekertugan.telegrambot.entity.fundamental.CommandCall;

public class CommandService {

    private ButtonRepository      bRepo = TelegramBotRepositoryProvider.getButtonRepository();
    private CommandCallRepository callRepo = TelegramBotRepositoryProvider.getCommandCallRepository();

    public AbstractCommand getCommandByButtonText(String buttonText, long chatId){

        Button button = bRepo.findFirstByButtonText(buttonText);

        CommandCall commandCall = callRepo.findOne(button.getCommandCallId());

        return CommandFactory.getCommand(commandCall.getCommandTypeId(),chatId, commandCall.getArguments());

    }

}
