package kz.rbots.bekertugan.front.view.dialogs;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.BotMessage;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.BotMessagesRepository;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.telegrambot.TelegramBot;
import kz.rbots.bekertugan.telegrambot.utils.TelegramBotExecutorUtil;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PreserveOnRefresh
class ChatWindow extends AbsoluteLayout implements Broadcaster.BotUpdatesListener, Broadcaster.TelegramBotTextMessageSendsListener {
    private final TextField textField = new TextField("Type text here: ");
    private Button sendButton = new Button("Send Message");
    private Button backToDialogs = new Button("Return");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private AbsoluteLayout enterTextLayout = new AbsoluteLayout();
    private DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
    private BotMessagesRepository botMessagesRepository = RepositoryProvider.getBotMessagesRepository();
    private Panel messagesPanel = new Panel();
    private VerticalLayout messagesLayout = new VerticalLayout();
    private final int MESSAGES_PER_PAGE = 17;
    private Long lastMessageId;
    private Button showMore;
    private String chatId;
    private boolean unlimitedMessagesPerPageAllowed;
    private Image avatar;

     ChatWindow(String chatId, Image avatar) {
        this.chatId = chatId;
        this.avatar = avatar;
        initEnterTextLayout(chatId);
        Broadcaster.register(this);
        Broadcaster.registerTextMessageListener(this);
        messagesPanel.setContent(messagesLayout);
        messagesPanel.setWidth("100%");
        messagesPanel.setHeight("80%");
        this.addComponent(messagesPanel);
        addHistoryIfExist(Long.parseLong(chatId));
        this.addComponent(enterTextLayout,"bottom: 0 px");
    }

    private void initEnterTextLayout(String chatId){
        //I don't know why idea light it
        //noinspection ConfusingArgumentToVarargsMethod
        textField.addShortcutListener(new ShortcutListener("enterListener", ShortcutAction.KeyCode.ENTER,null) {
            @Override
            public void handleAction(Object o, Object o1) {
                broadCastNewMessage(chatId);
            }
        });
        Dialog dialog = dialogRepository.findOne(Long.valueOf(chatId));
        initAvatar(dialog);

        sendButton.addClickListener(e-> getUI().access(()->broadCastNewMessage(chatId)));

        backToDialogs.addClickListener(e-> {
            Broadcaster.unregister(this);
            Broadcaster.unregisterTextMessageListener(this);
            BotBoardEventBus.post(new BotBoardEvent.BackToDialogsFromChatEvent());
        });

        textField.setWidth("80%");
        textField.setHeight("70%");
        enterTextLayout.setHeight("150px");
        enterTextLayout.setWidth("100%");
        enterTextLayout.addComponent(avatar,"top: 15%; left: 1%");
        enterTextLayout.addComponent(textField,"left: 130px; bottom: 10px");
        enterTextLayout.addComponent(sendButton,"left: 130px; bottom: 5%");
        enterTextLayout.addComponent(backToDialogs,"right: 0px ; top: 0px");
    }

    private void initAvatar(Dialog dialog){
        if (avatar!=null) {
            //noinspection ConstantConditions
            avatar.setSource(new ExternalResource(TelegramBotExecutorUtil.getActualURLForAvatar
                    (Integer.valueOf(chatId))));

        } else {
            avatar = new Image(dialog.getFirstNameAndLast());
            avatar.setSource(new ExternalResource("https://pickaface.net/assets/images/not-found.jpg"));
        }
        avatar.setWidth("110px");
        avatar.setHeight("110px");
    }

    private void addHistoryIfExist(long chatId){
        List<BotMessage> botMessages;

        try (Stream<BotMessage> messageStream = botMessagesRepository.findTop17ByChatIdOrderByMessageIdDesc(chatId).stream()) {
            botMessages = messageStream.collect(Collectors.toList());
            if (!botMessages.isEmpty()){
                botMessages.sort((Comparator.comparingLong(BotMessage::getMessageId)));
                botMessages.forEach(this::initMessages);
                lastMessageId = botMessages.get(0).getMessageId();
                if (botMessages.size()>=MESSAGES_PER_PAGE){
                    addShowMoreButtonToPanel();
                }

            }
        }
    }

    private void addShowMoreButtonToPanel(){
        showMore = new Button("Show More");
        showMore.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        showMore.addClickListener((Button.ClickEvent e)->
        {
            getUI().access(this::loadMoreMessages);
            unlimitedMessagesPerPageAllowed = true;
        });
        messagesLayout.addComponent(showMore,0);
    }

    private void broadCastNewMessage(String chatId){

        String messageText = textField.getValue();
        Broadcaster.broadcastToBot(
                new SendMessage(chatId,messageText));
        //TODO Сделать отображение имени бота при отправлении - правильным
        getUI().access(()->{postMessage(
                TelegramBot.getBotName(),
                LocalDateTime.now(),
                messageText
        );
            textField.clear();});
    }

    @Override
    public void receiveBroadcast(Update update) {
        lastMessageId = Long.valueOf(update.getMessage().getMessageId());
        if (chatId.equals(String.valueOf(update.getMessage().getChatId())))
        getUI().access(() -> postMessage(
                update.getMessage().getFrom().getFirstName(),
                LocalDateTime.now(),
                update.getMessage().getText()
        ));

    }

    private void postMessage(String from, LocalDateTime when, String text) {
        if (!unlimitedMessagesPerPageAllowed) {
            if (messagesLayout.getComponentCount() >= MESSAGES_PER_PAGE) {
                if (showMore != null) {
                    messagesLayout.removeComponent(messagesLayout.getComponent(1));
                } else {
                    addShowMoreButtonToPanel();
                    messagesLayout.removeComponent(messagesLayout.getComponent(0));
                }
            }
        }
        addMessageToLayoutInColors(new BotMessage(from,when,text),false);
        //Какой то ебучий костыль чтобы матать вниз
        messagesPanel.setScrollTop(1000000);
    }

    private void initMessages(BotMessage b) {
        if (messagesLayout.getComponentCount() >= MESSAGES_PER_PAGE) {
            messagesLayout.removeComponent(messagesLayout.getComponent(1));
        }
        addMessageToLayoutInColors(b,false);

    }

    private void loadMoreMessages() {
        List<BotMessage> botMessages = botMessagesRepository.findFirst20ByChatIdAndMessageIdBeforeOrderByMessageIdDesc(Long.valueOf(chatId),lastMessageId);
        botMessages.forEach(x->{
            lastMessageId = x.getMessageId();
            addMessageToLayoutInColors(x,true);
        });
        if (botMessages.isEmpty()) messagesLayout.removeComponent(showMore);
    }

    private String getDialogMessageString(String from, LocalDateTime when , String message){
        //На самом деле идея пиздит, и со стрингой оно будет работать дольше
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(when.format(formatter)).append("] ").append(from).append(": ").append(message);
        return sb.toString();
    }

    private void addMessageToLayoutInColors(BotMessage bm, boolean placeToIndexOne){
        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        //Distinct colors on label for user and bot
        if (!bm.getSenderName().equals(TelegramBot.getBotName())) {
            label.setValue("<font size = \"4\" color=\"DodgerBlue\">" +
                    getDialogMessageString(
                            bm.getSenderName(),
                            bm.getSendsDate(),
                            bm.getMessage()));

        } else {
            label.setValue("<font size = \"4\" color=\"Gray\">" +
                    getDialogMessageString(
                            bm.getSenderName(),
                            bm.getSendsDate(),
                            bm.getMessage()));
        }
        if (!placeToIndexOne){
            messagesLayout.addComponent(label);
        } else {
            messagesLayout.addComponent(label,1);
        }
    }
    //If ve have new sent message  with text we should show that to user
    @Override
    public void receiveBroadCast(Message message) {
        getUI().access(() -> postMessage(message.getFrom().getUserName(),LocalDateTime.now(),message.getText()));
    }
}