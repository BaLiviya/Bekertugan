package kz.rbots.bekertugan.front.view.dialogs;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.mock.DummyDialogData;
import kz.rbots.bekertugan.telegrambot.TelegramBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Dialogs extends Panel implements Broadcaster.TelegramDialogsUpdateListener {
    private VerticalLayout dialogsLayout = new VerticalLayout();
    private boolean listIsEmpty;
    private boolean inChatWindow;
    public Dialogs() {
        super();
        DummyDialogData.getAllDialogs().forEach(x->addNewDialog(x, TelegramBot.getToken()));
        this.setSizeFull();
        if (dialogsLayout.getComponentCount()==0){
            Label noDialogs = new Label("Here is no dialogsLayout");
            listIsEmpty = true;
            setContent(noDialogs);
        } else {
            setContent(dialogsLayout);
        }
        Broadcaster.registerDialogListener(this);
    }

    @Override
    public Registration addComponentDetachListener(ComponentDetachListener listener) {
      return (Registration) this::onDetach;
    }

    private void onDetach(){
        Broadcaster.unregisterDialogsListener(this);
        System.out.println("mda");
    }


    @Override
    public void receiveBroadcast(Dialog dialog, String botToken) {
        if (!inChatWindow) {
            System.out.println("HaveBroadcast");
            if (listIsEmpty) {
                listIsEmpty = false;
                addNewDialog(dialog, botToken);
                setContent(dialogsLayout);
            } else {
                addNewDialog(dialog, botToken);
            }
            getUI().access(() -> dialogsLayout.addComponent(new Label()));
        }
    }

    private void addNewDialog(Dialog dialog,String botToken){
        VerticalLayout dialogLayout = new VerticalLayout();
        ExternalResource externalResource = new ExternalResource("https://api.telegram.org/file/bot"
        + botToken +"/" + dialog.getAvatarFileId());
        dialogLayout.setId(String.valueOf(dialog.getChatId()));
        Image image = new Image(dialog.getNameAndLastname(),externalResource);
        image.setHeight("100px");
        image.setWidth("100px");
        Button dialogButton = new Button("@"+dialog.getUserName());
        dialogButton.setId(String.valueOf(dialog.getChatId()));
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponents(image,dialogButton);
        dialogsLayout.addComponents(dialogLayout);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        String chatId = e.getButton().getId();
        getUI().access(()->setContent(new ChatWindow(chatId)));
        Broadcaster.unregisterDialogsListener(this);
    }

    private class ChatWindow extends VerticalLayout implements Broadcaster.BotUpdatesListener {
        private final String chatId;
        private final TextField textField = new TextField("Type text here: ");
        Button sendButton = new Button("Send Message");
        Button backToDialogs = new Button("Return");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        HorizontalLayout enterTextLayout = new HorizontalLayout();


        private ChatWindow(String chatId) {

            this.chatId = chatId;
            sendButton.addClickListener(e-> {
                Broadcaster.broadcastToBot(
                    new SendMessage(chatId,textField.getValue()));
            getUI().access(()->postMessage("[" +LocalDateTime.now().format(formatter)+"] Вы: " + textField.getValue()));});
            backToDialogs.addClickListener(e-> {
                inChatWindow=false;
                getUI().access(()->setContent(dialogsLayout));
                Broadcaster.unregister(this);
                detach();
            });
            enterTextLayout.addComponent(textField);
            enterTextLayout.addComponent(sendButton);
            enterTextLayout.addComponent(backToDialogs);
            Broadcaster.register(this);
            this.addComponent(enterTextLayout);
        }


        @Override
        public void receiveBroadcast(Update update) {

            getUI().access(() -> postMessage("["+LocalDateTime.now().format(formatter)+ "] " +update.getMessage().getFrom().getFirstName() + ": "
                    + update.getMessage().getText()));
        }

        private void postMessage(String s) {

        if (this.getComponentCount() > 10) {
            this.removeComponent(this.getComponent(1));
        }
        this.addComponent(new HorizontalLayout(new Label(s)));


    }


    }
}
