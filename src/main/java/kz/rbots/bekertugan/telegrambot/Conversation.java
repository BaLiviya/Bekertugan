package kz.rbots.bekertugan.telegrambot;


import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import kz.rbots.bekertugan.telegrambot.command.AbstractCommand;
import kz.rbots.bekertugan.telegrambot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.objects.Update;


@RequiredArgsConstructor
@Slf4j
class Conversation {
    private final boolean          isGroup;
    private final TelegramBot      bot;
    private final Long             chatId;
    private final DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
    private final CommandService   commandService   = new CommandService();
    private       AbstractCommand  command;



    void operate(Update update){

        if (command!=null){

            if (command.execute(update, bot)){

                command = null;

            }

            return;

        }

        if (!isGroup) {

            command = getCommand(update);

            if (command.execute(update, bot)){

                command = null;

            }

        }

    }

    private AbstractCommand getCommand(Update update){

        if (update.hasMessage() && update.getMessage().hasText()){

            return commandService.getCommandByButtonText(update.getMessage().getText(),chatId);

        }

        if (update.hasCallbackQuery()){

            return commandService.getCommandByButtonText(update.getCallbackQuery().getData(),chatId);

        }

        throw new IllegalArgumentException("Arguments for Command call was not found");


    }



}
