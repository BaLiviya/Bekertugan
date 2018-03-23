package kz.rbots.bekertugan.front.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryProvider {
    private static BotMessagesRepository botMessagesRepository;
    private static DialogRepository dialogRepository;

    @SuppressWarnings("AccessStaticViaInstance")
    @Autowired
    public RepositoryProvider(BotMessagesRepository botMessagesRepository, DialogRepository dialogRepository) {
        this.botMessagesRepository = botMessagesRepository;
        this.dialogRepository = dialogRepository;
    }


    public static BotMessagesRepository getBotMessagesRepository() {
        return botMessagesRepository;
    }

    public static DialogRepository getDialogRepository() {
        return dialogRepository;
    }
}
