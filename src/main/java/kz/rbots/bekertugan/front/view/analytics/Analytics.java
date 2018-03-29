package kz.rbots.bekertugan.front.view.analytics;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.BotMessagesRepository;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import org.telegram.telegrambots.api.objects.Update;

public class Analytics extends GridLayout implements View, Broadcaster.BotUpdatesListener, Broadcaster.TelegramDialogsUpdateListener {
    private DialogRepository      dialogRepository = RepositoryProvider.getDialogRepository();
    private BotMessagesRepository botMessagesRepository = RepositoryProvider.getBotMessagesRepository();
    private long allMessagesCount = botMessagesRepository.count();
    private long allDialogsCount  = dialogRepository.count();
    private Label messagesText;
    private Label messagesCount;
    private Label dialogText;
    private Label dialogCount;

    public Analytics() {
        super(2, 2);
        this.setSizeFull();
        initAllMessagesCount();
        initAllDialogsCount();
    }

    private void initAllMessagesCount(){
        messagesText = new Label("Messages count : ");
        this.addComponent(messagesText,0,0);
        messagesCount = new Label(String.valueOf(allMessagesCount));
        this.addComponent(messagesCount,1,0);
    }

    private void initAllDialogsCount(){
        dialogText = new Label("Dialogs count : ");
        this.addComponent(dialogText, 0, 1);
        dialogCount = new Label(String.valueOf(allDialogsCount));
        this.addComponent(dialogCount,1,1);
    }

    @Override
    public void detach() {
        Broadcaster.unregister(this);
        Broadcaster.unregisterDialogsListener(this);
        super.detach();
    }


    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        Broadcaster.register(this);
        Broadcaster.registerDialogListener(this);
    }

    @Override
    public void receiveBroadcast(Update update) {
        allMessagesCount++;
        getUI().access(()->messagesCount.setValue(String.valueOf(allMessagesCount)));
    }

    @Override
    public void receiveBroadcast(Dialog dialog) {
        allDialogsCount++;
        getUI().access(()->dialogCount.setValue(String.valueOf(allDialogsCount)));
    }
}
