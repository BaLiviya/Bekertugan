package kz.rbots.bekertugan.front.view.dialogs;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.BotMessage;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.BotMessagesRepository;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import kz.rbots.bekertugan.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@PreserveOnRefresh
public class DialogsPanel extends Panel implements Broadcaster.TelegramDialogsUpdateListener {
    private VerticalLayout dialogsLayout = new VerticalLayout();
    private boolean listIsEmpty;
    private boolean inChatWindow;



    public DialogsPanel() {
        super();
        DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
        dialogRepository.findAll().forEach(x->addNewDialog(x, TelegramBot.getToken()));
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
        if (dialog.getAvatarFileId()!=null) {
            ExternalResource externalResource = new ExternalResource("https://api.telegram.org/file/bot"
                    + botToken + "/" + dialog.getAvatarFileId());
            Image image = new Image(dialog.getFirstNameAndLast(),externalResource);
            image.setHeight("100px");
            image.setWidth("100px");
            dialogLayout.addComponent(image);
        }
        dialogLayout.setId(String.valueOf(dialog.getChatId()));
        String dialogButtonText = dialog.getUserName() != null ? "@" + dialog.getUserName() : dialog.getFirstName();
        Button dialogButton = new Button(dialogButtonText);
        dialogButton.setId(String.valueOf(dialog.getChatId()));
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponent(dialogButton);
        dialogsLayout.addComponents(dialogLayout);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        String chatId = e.getButton().getId();
        getUI().access(()->setContent(new ChatWindow(chatId)));
        Broadcaster.unregisterDialogsListener(this);
    }

    @PreserveOnRefresh
    private class ChatWindow extends VerticalLayout implements Broadcaster.BotUpdatesListener {
        private Image avatar;
        private final TextField textField = new TextField("Type text here: ");
        private Button sendButton = new Button("Send Message");
        private Button backToDialogs = new Button("Return");
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        private AbsoluteLayout enterTextLayout = new AbsoluteLayout();
        private DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
        private BotMessagesRepository botMessagesRepository = RepositoryProvider.getBotMessagesRepository();
        private int MESSAGES_PER_PAGE = 17;

        private ChatWindow(String chatId) {
            initEnterTextLayout(chatId);
            Broadcaster.register(this);
            this.addComponent(enterTextLayout);
            addHistoryIfExist(Long.parseLong(chatId));
        }

        private void initEnterTextLayout(String chatId){
            textField.addShortcutListener(new ShortcutListener("enterListener",ShortcutAction.KeyCode.ENTER,null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    broadCastNewMessage(chatId);
                }
            });
            Dialog dialog = dialogRepository.findOne(Long.valueOf(chatId));
//                    DummyDialogData.getDialogByChatId(Long.parseLong(chatId));
            ExternalResource externalResource = new ExternalResource("https://api.telegram.org/file/bot"
                    + TelegramBot.getToken() +"/"
                    + dialog.getAvatarFileId());
            avatar = new Image(dialog.getFirstNameAndLast(),externalResource);
            avatar.setWidth("75px");
            avatar.setHeight("75px");

            sendButton.addClickListener(e-> getUI().access(()->broadCastNewMessage(chatId)));

            backToDialogs.addClickListener(e-> {
                inChatWindow=false;
                getUI().access(()->setContent(dialogsLayout));
                Broadcaster.unregister(this);
            });
            textField.setWidth("90%");
            enterTextLayout.setHeight("120px");
            enterTextLayout.setWidth("100%");
            enterTextLayout.addComponent(avatar,"top: 15%");
            enterTextLayout.addComponent(textField,"left: 80px; top: 25%");
            enterTextLayout.addComponent(sendButton,"left: 80px; bottom: 0px");
            enterTextLayout.addComponent(backToDialogs,"right: 0px ; top: 0px");
        }

        void addHistoryIfExist(long chatId){
            List<BotMessage> botMessages;

            try (Stream<BotMessage> messageStream = botMessagesRepository.findAllByChatId(chatId).stream()) {
                botMessages = messageStream.collect(Collectors.toList());
                if (!botMessages.isEmpty()){
                    botMessages.stream().limit(MESSAGES_PER_PAGE-1).sorted((o1, o2) ->
                            o1.getSendsDate().isBefore(o2.getSendsDate()) ?
                                    1 : 0).forEach(
                            x->postMessage("[" + x.getSendsDate().format(formatter) + "] " + x.getSenderName() + ": " + x.getMessage()));
                }
            }
        }

        private void broadCastNewMessage(String chatId){

            String messageText = textField.getValue();
            Broadcaster.broadcastToBot(
                    new SendMessage(chatId,messageText));
            //TODO Сделать отображение имени бота при отправлении - правильным
            getUI().access(()->{postMessage("[" +
                    LocalDateTime.now().format(formatter)+
                    "] Бот: " + messageText);
            textField.clear();});
        }

        @Override
        public void receiveBroadcast(Update update) {

            getUI().access(() -> postMessage("["+LocalDateTime.now().format(formatter)+ "] " +update.getMessage().getFrom().getFirstName() + ": "
                    + update.getMessage().getText()));

        }

        private void postMessage(String s) {
        if (this.getComponentCount() > MESSAGES_PER_PAGE) {
            this.removeComponent(this.getComponent(1));
        }
        HorizontalLayout newLine = new HorizontalLayout();
        newLine.addComponent(new Label(s));
        this.addComponent(newLine);


    }


    }
}
