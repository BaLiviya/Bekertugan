package kz.rbots.bekertugan.front.view.dialogs;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import kz.rbots.bekertugan.broadcaster.Broadcaster;
import kz.rbots.bekertugan.entities.Dialog;
import kz.rbots.bekertugan.front.data.DialogRepository;
import kz.rbots.bekertugan.front.data.RepositoryProvider;
import kz.rbots.bekertugan.front.event.BotBoardEvent;
import kz.rbots.bekertugan.front.event.BotBoardEventBus;
import kz.rbots.bekertugan.telegrambot.utils.TelegramBotExecutorUtil;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@PreserveOnRefresh
public class DialogsPanel extends Panel implements Broadcaster.TelegramDialogsUpdateListener, Broadcaster.BotUpdatesListener {
    private AbsoluteLayout dialogsLayout = new AbsoluteLayout();
    private int xOffset;
    private int yOffSet;
    private final int DIALOGS_PER_ROW = 7;
    private int lastDialogNumberInROw;
    private ConcurrentHashMap<Long, Image> avatars;
    private ConcurrentLinkedDeque<VerticalLayout> dialogList;



    public DialogsPanel() {
        super();
        dialogList = new ConcurrentLinkedDeque<>();
        avatars = new ConcurrentHashMap<>();
        BotBoardEventBus.register(this);
        initDialogs();
        Broadcaster.registerDialogListener(this);
        Broadcaster.register(this);
    }
    //Because idea lies
    @SuppressWarnings("unused")
    @Subscribe
    public void comeBackFromChatWindow(final BotBoardEvent.BackToDialogsFromChatEvent e){
        getUI().access(()->setContent(dialogsLayout));
    }

    private void initDialogs(){
        DialogRepository dialogRepository = RepositoryProvider.getDialogRepository();
        List<Dialog>dialogs = Lists.newArrayList(dialogRepository.findAllByOrderMessageISNewer());
        this.setSizeFull();
        if (dialogs.isEmpty()){
            Label noDialogs = new Label("Here is no dialogs");
            setContent(noDialogs);
        } else {
            dialogs.forEach(x->addDialogToTail(getDialogLayOut(x)));
            setContent(dialogsLayout);
        }
    }


    @Override
    public void detach() {
        BotBoardEventBus.unregister(this);
        Broadcaster.unregisterDialogsListener(this);
        Broadcaster.unregister(this);
        super.detach();
    }

    @Override
    public void receiveBroadcast(Dialog dialog) {
            addDialogToHead(getDialogLayOut(dialog));
            getUI().access(() -> setContent(dialogsLayout));
    }

    private void addDialogToTail(VerticalLayout dialog){
        addDialogLayoutToDialogsLayout(dialog);
        dialogList.addLast(dialog);
    }

    private void addDialogToHead(VerticalLayout dialog){
        if (dialogList.contains(dialog)){
            dialogList.remove(dialog);
        }
        dialogList.addFirst(dialog);
        dialogsLayout.removeAllComponents();
        lastDialogNumberInROw = 0;
        xOffset = 0;
        yOffSet = 0;
        dialogList.forEach(this::addDialogLayoutToDialogsLayout);
    }

    private VerticalLayout getDialogLayOut(Dialog dialog){
        VerticalLayout dialogLayout = new VerticalLayout();
        Image avatar;
        if (dialog.getAvatarFileId()!=null) {
            avatar = getAvatarViaURL(TelegramBotExecutorUtil.getActualURLForAvatar(Math.toIntExact(dialog.getChatId())));
        } else {
            avatar = getAvatarViaURL("https://pickaface.net/assets/images/not-found.jpg");
        }
        avatars.put(dialog.getChatId(),avatar);
        dialogLayout.addComponent(avatar);
        dialogLayout.setId(String.valueOf(dialog.getChatId()));
        String dialogButtonText = dialog.getUserName() != null ? "@" + dialog.getUserName() : dialog.getFirstName();
        Button dialogButton = new Button(dialogButtonText);
        Label label = new Label(dialog.getFirstNameAndLast());
        dialogLayout.addComponent(label);
        String chatIdForID = String.valueOf(dialog.getChatId());
        dialogLayout.setId(chatIdForID);
        dialogButton.setId(chatIdForID);
        dialogButton.addClickListener(this::loginButtonClick);
        dialogLayout.addComponent(dialogButton);
        return dialogLayout;
    }

    private void addDialogLayoutToDialogsLayout(VerticalLayout dialogLayout){
        if (lastDialogNumberInROw == DIALOGS_PER_ROW) {
            lastDialogNumberInROw = 0;
            yOffSet = yOffSet + 250;
            xOffset = 0;
        }
        lastDialogNumberInROw++;
        dialogsLayout.addComponent(dialogLayout, "left:" + xOffset + "px;" + "top:" + yOffSet + "px");
        xOffset = xOffset + 130;
    }

    private void loginButtonClick(Button.ClickEvent e) {
        String chatId = e.getButton().getId();
        Image a = new Image();
        a.setSource(avatars.get(Long.valueOf(chatId)).getSource());
        getUI().access(()->setContent(new ChatWindow(chatId, a)));
        Broadcaster.unregisterDialogsListener(this);
    }

    private Image getAvatarViaURL(String imageUrl){
        ExternalResource externalResource = new ExternalResource(imageUrl);
        Image image = new Image();
        image.setSource(externalResource);
        image.setHeight("120px");
        image.setWidth("120px");
        return image;
    }

    @Override
    public void receiveBroadcast(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());

        //Check if dialog already in head in case of another broadcast (dialogs broadcast)
        if (!dialogList.getFirst().getId().equals(chatId)) {
            //noinspection ConstantConditions
            VerticalLayout updatedDialog = dialogList.stream().filter(x -> x.getId().equals(chatId)).findFirst().get();
            addDialogToHead(updatedDialog);
            getUI().access(()->setContent(dialogsLayout));
        }

    }
}
