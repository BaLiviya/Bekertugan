package kz.rbots.bekertugan.telegrambot.command;

import kz.rbots.bekertugan.telegrambot.command.impl.SendMessageWithTextFromArguments;

public class CommandFactory {

    public static AbstractCommand getCommand(int commandId, long chatId, String arguments){

        switch (commandId){

            case 1:
                return new SendMessageWithTextFromArguments(chatId, arguments);


            default:
                throw new IllegalArgumentException("Command with that id - " + commandId + " has not found");

        }
    }

}
